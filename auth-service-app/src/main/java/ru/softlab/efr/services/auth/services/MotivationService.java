package ru.softlab.efr.services.auth.services;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.softlab.efr.services.auth.exchange.model.CheckModel;
import ru.softlab.efr.services.auth.exchange.model.ErrorModel;
import ru.softlab.efr.services.auth.exchange.model.MotivationCorrectStatus;
import ru.softlab.efr.services.auth.exchange.model.MotivationDocumentTypes;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.MotivationEntity;
import ru.softlab.efr.services.auth.model.MotivationReportModel;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.MotivationRepository;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserRepository;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MotivationService {

    private static final Logger LOGGER = Logger.getLogger(MotivationService.class);

    private PrincipalDataSource principalDataSource;
    private MotivationRepository motivationRepository;
    private MotivationAttachmentService motivationAttachmentService;
    private UserRepository userRepository;
    private MotivationTimeControlService motivationTimeControlService;
    private MotivationXlsxReporterService motivationXlsxReporterService;
    private MotivationReporterService motivationReporterService;

    @Autowired
    public MotivationService(PrincipalDataSource principalDataSource,
                             MotivationRepository motivationRepository,
                             MotivationAttachmentService motivationAttachmentService,
                             UserRepository userRepository,
                             MotivationTimeControlService motivationTimeControlService,
                             MotivationXlsxReporterService motivationXlsxReporterService,
                             MotivationReporterService motivationReporterService) {
        this.principalDataSource = principalDataSource;
        this.motivationRepository = motivationRepository;
        this.motivationAttachmentService = motivationAttachmentService;
        this.userRepository = userRepository;
        this.motivationTimeControlService = motivationTimeControlService;
        this.motivationXlsxReporterService = motivationXlsxReporterService;
        this.motivationReporterService = motivationReporterService;
    }

    @Transactional
    public byte[] getAdminReport(LocalDate startDate, LocalDate endDate) {
        if (Objects.isNull(startDate)) {
            startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        }
        if (Objects.isNull(endDate)) {
            endDate = LocalDate.now().withDayOfMonth(1);
        }
        final List<MotivationCorrectStatus> correctStatuses = Collections.unmodifiableList(
                Arrays.asList(MotivationCorrectStatus.CORRECT, MotivationCorrectStatus.INCORRECT, MotivationCorrectStatus.NOT_CHECKED));
        List<MotivationEntity> motivationList = motivationRepository.getReportMotivations(true, startDate, endDate, correctStatuses);
        List<MotivationEntity> activeList = new ArrayList<>();
        motivationList.forEach(motivationEntity -> {
            if (Objects.nonNull(motivationEntity.getEndDate())) {
                checkMotivationExpiration(motivationEntity);
            } else {
                motivationEntity.setEndDate(motivationTimeControlService.getMotivationEndDate(motivationEntity.getStartDate()));
                motivationEntity = checkMotivationExpiration(motivationEntity);
            }
            if (motivationEntity.isActive()) {
                activeList.add(motivationEntity);
            }
        });
        List<MotivationReportModel> motivationReport = motivationReporterService.getReportByActiveList(activeList);
        return motivationXlsxReporterService.getReport(motivationReport, startDate, endDate);
    }

    @Transactional
    public MotivationEntity getMotivationByUserId(Long userId) {
        MotivationEntity entity = motivationRepository.getByUserAndIsActive(
                userRepository.findById(userId),
                true);
        return getMotivationEntity(entity);
    }

    @Transactional
    public MotivationEntity getMotivation() {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        MotivationEntity entity = motivationRepository.getByUserAndIsActive(
                userRepository.findByIdAndDeleted(principalData.getId(), false),
                true);
        return getMotivationEntity(entity);
    }

    @Transactional
    public List<MotivationEntity> getMotivationsListByUserId(Long userId) {
        List<MotivationEntity> motivationsList = motivationRepository.getByUser(userRepository.findById(userId));
        if (!motivationsList.isEmpty()) {
            motivationsList.forEach(motivationEntity -> {
                if (Objects.nonNull(motivationEntity.getEndDate())) {
                    checkMotivationExpiration(motivationEntity);
                } else {
                    motivationEntity.setEndDate(motivationTimeControlService.getMotivationEndDate(motivationEntity.getStartDate()));
                    checkMotivationExpiration(motivationEntity);
                }
            });
        }
        return motivationsList;
    }

    @Transactional
    public Object addOrModifyMotivation(
            String accountNumber, String bikCode, String bankName, String inn,
            String registrationAddress, String index, LocalDate startDate) {
        ErrorModel motivationCheckModel = new ErrorModel();
        PrincipalData principalData = principalDataSource.getPrincipalData();
        Employee user = userRepository.findByIdAndDeleted(principalData.getId(), false);
        MotivationEntity motivation = Optional.ofNullable(motivationRepository.getByUserAndIsActive(
                user,
                true
        )).orElse(new MotivationEntity());
        if (MotivationCorrectStatus.CORRECT.equals(motivation.getMotivationCorrectStatus())) {
            LOGGER.info(String.format("Программа мотивации %s подтверждена, редактирование невозможно", motivation.getId()));
            return null;
        } else {
            motivation.setUser(user);
            if (Objects.isNull(startDate)) {
                motivation.setStartDate(LocalDate.now());
                startDate = LocalDate.now();
            } else {
                motivationCheckModel = motivationTimeControlService.getMotivationStartDate(user.getId(), startDate);
            }
            if (Objects.isNull(inn)) {
                if (StringUtils.isEmpty(registrationAddress)|| StringUtils.isEmpty(index)) {
                    CheckModel checkModel = new CheckModel();
                    checkModel.setKey("Проверка валидности полей с данными");
                    checkModel.setValue("При отсутствии ИНН требуется указать адрес регистрации и индекс");
                    motivationCheckModel.addErrorsItem(checkModel);
                    LOGGER.info("При отсутствии ИНН требуется указать адрес регистрации и индекс");
                }
            }
            if (CollectionUtils.isEmpty(motivationCheckModel.getErrors())) {
                motivation.setStartDate(startDate);
            } else {
                return motivationCheckModel;
            }
            motivation.setEndDate(motivationTimeControlService.getMotivationEndDate(startDate));
            motivation.setAccountNumber(accountNumber);
            motivation.setBikCode(bikCode);
            motivation.setBankName(bankName);
            motivation.setInn(inn);
            motivation.setRegistrationAddress(registrationAddress);
            motivation.setIndex(index);
            motivation.setMotivationCorrectStatus(MotivationCorrectStatus.NOT_CHECKED);
            motivation.setActive(true);
            motivation.setLastEdited();
            motivation = motivationRepository.save(motivation);
            return checkMotivationExpiration(motivation);
        }
    }

    public boolean attachDocument(MultipartFile content, MotivationDocumentTypes motivationDocumentTypes) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        Employee user = userRepository.findByIdAndDeleted(principalData.getId(), false);
        MotivationEntity motivation = motivationRepository.getByUserAndIsActive(user, true);
        if (Objects.nonNull(motivation)) {
            if (!MotivationCorrectStatus.CORRECT.equals(motivation.getMotivationCorrectStatus())) {
                if (motivationAttachmentService.attachOrModify(user, motivation, content, motivationDocumentTypes)) {
                    motivation.setMotivationCorrectStatus(MotivationCorrectStatus.NOT_CHECKED);
                    motivation.setLastEdited();
                    motivationRepository.save(motivation);
                    return true;
                } else {
                    LOGGER.error(String.format("Не удалось прикрепить файл к программе мотивации %s", motivation.getId()));
                    return false;
                }
            }
            LOGGER.info(String.format("Программа мотивации %s уже проверена", motivation.getId()));
            return false;
        }
        LOGGER.warn(String.format("Программа мотивации не найдена для пользователя %s", user.getEmail()));
        return false;
    }

    public String getFilenameByMotivationId(Long id, MotivationDocumentTypes motivationDocumentTypes) {
        MotivationEntity motivation = motivationRepository.getById(id);
        if (Objects.nonNull(motivation)) {
            return motivationAttachmentService.getFilenameByMotivation(motivation, motivationDocumentTypes);
        }
        LOGGER.info(String.format("Программа мотивации %s не найдена", id));
        return null;
    }

    public String getFilenameForUser(MotivationDocumentTypes motivationDocumentTypes) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        Employee user = userRepository.findByIdAndDeleted(principalData.getId(), false);
        MotivationEntity motivation = motivationRepository.getByUserAndIsActive(user, true);
        if (Objects.nonNull(motivation)) {
            return motivationAttachmentService.getFilenameByUserAndMotivation(user, motivation, motivationDocumentTypes);
        }
        LOGGER.warn(String.format("Программа мотивации не найдена для пользователя %s", user.getEmail()));
        return null;
    }

    public byte[] getMotivationFileForUser(MotivationDocumentTypes motivationDocumentTypes) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        Employee user = userRepository.findByIdAndDeleted(principalData.getId(), false);
        MotivationEntity motivation = motivationRepository.getByUserAndIsActive(user, true);
        if (Objects.nonNull(motivation)) {
            return motivationAttachmentService.getFileByMotivationAndUser(user, motivation, motivationDocumentTypes);
        }
        LOGGER.warn(String.format("Программа мотивации не найдена для пользователя %s", user.getEmail()));
        return null;
    }

    public byte[] getMotivationFileByMotivation(Long motivationId, MotivationDocumentTypes motivationDocumentTypes) {
        MotivationEntity motivation = motivationRepository.getById(motivationId);
        if (Objects.nonNull(motivation)) {
            return motivationAttachmentService.getFileByMotivation(motivation, motivationDocumentTypes);
        }
        LOGGER.info(String.format("Программа мотивации %s не найдена", motivationId));
        return null;
    }

    @Transactional
    public ErrorModel modifyMotivationAdmin(Long motivationId, String accountNumber, String bankName, String bikCode,
                                            String inn, String registrationAddress, String index, String comment,
                                            MotivationCorrectStatus motivationCorrectStatus, LocalDate startDate) {
        ErrorModel errorModel = new ErrorModel();
        MotivationEntity motivation = motivationRepository.getById(motivationId);
        if (Objects.nonNull(accountNumber)) {
            motivation.setAccountNumber(accountNumber);
        }
        if (Objects.nonNull(bankName)) {
            motivation.setBankName(bankName);
        }
        if (Objects.nonNull(bikCode)) {
            motivation.setBikCode(bikCode);
        }
        if (Objects.nonNull(inn)) {
            motivation.setInn(inn);
        }
        if (Objects.nonNull(registrationAddress)) {
            motivation.setRegistrationAddress(registrationAddress);
        }
        if (Objects.nonNull(index)) {
            motivation.setIndex(index);
        }
        if (Objects.nonNull(comment)) {
            motivation.setComment(comment);
        }
        if (Objects.nonNull(startDate)) {
            ErrorModel motivationTimeServiceResponse = motivationTimeControlService.getMotivationStartDate(motivation.getUser().getId(), startDate);
            if (motivationTimeServiceResponse.getErrors().isEmpty()) {
                motivation.setStartDate(startDate);
            } else {
                return motivationTimeServiceResponse;
            }
        }
        if (Objects.nonNull(motivationCorrectStatus)) {
            if (MotivationCorrectStatus.CORRECT.equals(motivationCorrectStatus)) {
                if (isMotivationFilled(motivation)) {
                    motivation.setComment(null);
                    motivation.setMotivationCorrectStatus(motivationCorrectStatus);
                } else {
                    LOGGER.info(String.format("Заполнены не все поля, необходимые для присвоения статуса корректности для мотивации %s", motivation.getId()));
                    errorModel.setErrors(getMotivationErrors(motivation));
                    return errorModel;
                }
            } else {
                motivation.setMotivationCorrectStatus(motivationCorrectStatus);
            }
        }
        motivation = motivationRepository.save(motivation);
        checkMotivationExpiration(motivation);
        return errorModel;
    }

    @Transactional(readOnly = true)
    public boolean isCurrentUserMotivationFilled() {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        Employee user = userRepository.findByIdAndDeleted(principalData.getId(), false);
        MotivationEntity motivation = motivationRepository.getByUserAndIsActive(user, true);

        if (isMotivationFilled(motivation)) {
            return !MotivationCorrectStatus.INCORRECT.equals(motivation.getMotivationCorrectStatus());
        }
        return false;
    }

    @Transactional(readOnly = true)
    public boolean isMotivationFilled(MotivationEntity motivation) {
        if (Objects.nonNull(motivation)) {
            if (Objects.nonNull(motivation.getAccountNumber()) && Objects.nonNull(motivation.getBankName()) && Objects.nonNull(motivation.getBikCode())) {
                if (Objects.nonNull(motivation.getInn())) {
                    if (Objects.nonNull(getFilenameByMotivationId(motivation.getId(), MotivationDocumentTypes.CHECK_FORM))) {
                        if (Objects.nonNull(getFilenameByMotivationId(motivation.getId(), MotivationDocumentTypes.PASSPORT))) {
                            return true;
                        }
                    }
                } else {
                    LOGGER.trace("Не указан ИНН, проверка по индексу и адресу регистрации");
                    if (Objects.nonNull(motivation.getIndex()) && Objects.nonNull(motivation.getRegistrationAddress())) {
                        if (Objects.nonNull(getFilenameByMotivationId(motivation.getId(), MotivationDocumentTypes.CHECK_FORM))) {
                            if (Objects.nonNull(getFilenameByMotivationId(motivation.getId(), MotivationDocumentTypes.PASSPORT))) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    protected List<CheckModel> getMotivationErrors(MotivationEntity motivation) {
        List<CheckModel> checklist = new ArrayList<>();
        CheckModel checkModel = new CheckModel();
        checkModel.setKey("Проверка заполненности полей данных мотивации");
        if (Objects.nonNull(motivation)) {
            if (Objects.isNull(motivation.getAccountNumber())) {
                checkModel.setValue("Не указан номер рассчетного счета");
                checklist.add(checkModel);
            }
            if (Objects.isNull(motivation.getBankName())) {
                checkModel.setValue("Не указано наиминование фелиала банка");
                checklist.add(checkModel);
            }
            if (Objects.isNull(motivation.getBikCode())) {
                checkModel.setValue("Не указан БИК код банка");
                checklist.add(checkModel);
            }
            if (!Objects.isNull(motivation.getInn())) {
                if (Objects.isNull(motivation.getIndex())) {
                    checkModel.setValue("Не указан индекс регистрации, при отсутствии ИНН");
                    checklist.add(checkModel);
                }
                if (Objects.isNull(motivation.getRegistrationAddress())) {
                    checkModel.setValue("Не указан адрес регистрации, при отсутствии ИНН");
                    checklist.add(checkModel);
                }
            }
            if (Objects.isNull(getFilenameByMotivationId(motivation.getId(), MotivationDocumentTypes.PASSPORT))) {
                checkModel.setValue("Не добавлен снимок паспорта");
                checklist.add(checkModel);
            }
            if (Objects.isNull(getFilenameByMotivationId(motivation.getId(), MotivationDocumentTypes.CHECK_FORM))) {
                checkModel.setValue("Не добавлен снимок подписанной формы соглашения на обработку личной информации");
                checklist.add(checkModel);
            }
        } else {
            checkModel.setValue("Не найдена форма мотивации в базе данных системы");
            checklist.add(checkModel);
        }
        return checklist;
    }

    private MotivationEntity getMotivationEntity(MotivationEntity entity) {
        if (Objects.nonNull(entity)) {
            if (Objects.nonNull(entity.getEndDate())) {
                entity = checkMotivationExpiration(entity);
            } else {
                entity.setEndDate(motivationTimeControlService.getMotivationEndDate(entity.getStartDate()));
                entity = checkMotivationExpiration(entity);
            }
        } else {
            return null;
        }
        if (entity.isActive()) {
            return entity;
        } else {
            return null;
        }
    }

    private MotivationEntity checkMotivationExpiration(MotivationEntity motivationEntity) {
        if (motivationTimeControlService.checkMotivationActiveStatus(motivationEntity.getEndDate())) {
            return motivationEntity;
        } else {
            LOGGER.info(String.format("Время действия мотивации %s истекло", motivationEntity.getId()));
            motivationEntity.setActive(false);
            return motivationRepository.save(motivationEntity);
        }
    }

    public String getAdminReportFilename() throws UnsupportedEncodingException {
        return URLEncoder.encode(String.format("Отчет по данным на мотивацию %s%s", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), ".xlsx"),
                StandardCharsets.UTF_8.name()).replaceAll("\\+", " ");
    }
}
