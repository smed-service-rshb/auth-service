package ru.softlab.efr.services.auth.services;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.exchange.model.MotivationCorrectStatus;
import ru.softlab.efr.services.auth.exchange.model.MotivationDocumentTypes;
import ru.softlab.efr.services.auth.model.*;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.MotivationAttachmentsRepository;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class MotivationReporterService {

    private MotivationAttachmentsRepository motivationAttachmentsRepository;

    @Autowired
    public MotivationReporterService(MotivationAttachmentsRepository motivationAttachmentsRepository) {
        this. motivationAttachmentsRepository =  motivationAttachmentsRepository;
    }

    private static final Map<MotivationCorrectStatus, String> motivationStatusesMap = Collections.unmodifiableMap(
            new HashMap<MotivationCorrectStatus, String>() {{
                put(MotivationCorrectStatus.CORRECT, "Да");
                put(MotivationCorrectStatus.INCORRECT, "Нет");
                put(MotivationCorrectStatus.NOT_CHECKED, "Не проверено");
            }});

    public List<MotivationReportModel> getReportByActiveList(List<MotivationEntity> activeList) {
        List<MotivationReportModel> responseList = new ArrayList<>();
        activeList.forEach(active -> {
            AtomicReference<String> latestAgreementUpdate = new AtomicReference<>();
            AtomicReference<String> latestPassportUpdate = new AtomicReference<>();
            List<MotivationAttachmentsEntity> documents = motivationAttachmentsRepository.getByMotivationEntityAndIsDeleted(active, false);
            if (CollectionUtils.isNotEmpty(documents)) {
                documents.forEach(document -> {
                    if (MotivationDocumentTypes.CHECK_FORM.equals(document.getMotivationDocumentTypes())) {
                        if (Objects.nonNull(document.getCreationDate())) {
                            latestAgreementUpdate.set(document.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                        }
                    } else if (MotivationDocumentTypes.PASSPORT.equals(document.getMotivationDocumentTypes())) {
                        if (Objects.nonNull(document.getCreationDate())) {
                            latestPassportUpdate.set(document.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                        }
                    }
                });
            }
            Employee employee = active.getUser();
            if (!employee.getOrgUnits().isEmpty()) {
                employee.getOrgUnits().forEach(orgUnit -> {
                    MotivationReportModel motivationReportModel = new MotivationReportModel();
                    motivationReportModel.setOffice(orgUnit.getName());
                    motivationReportModel.setEmployerSNM(String.format("%s %s%s",
                            Optional.ofNullable(employee.getSecondName()).orElse(StringUtils.EMPTY),
                            Optional.ofNullable(employee.getFirstName()).orElse(StringUtils.EMPTY),
                            StringUtils.isBlank(employee.getMiddleName()) ? StringUtils.EMPTY : " ".concat(employee.getMiddleName())
                    ));
                    motivationReportModel.setEmployerPersonnelNumber(employee.getPersonnelNumber());
                    motivationReportModel.setPosition(employee.getPosition());
                    if (Objects.nonNull((employee.getRoles()))) {
                        motivationReportModel.setEmployerRole(employee.getRoles()
                                .stream()
                                .distinct()
                                .map(Role::getName)
                                .collect(Collectors.joining(", ")));
                    }
                    motivationReportModel.setEmployerSegment(employee.getSegment().getName());
                    motivationReportModel.setAccountNumber(active.getAccountNumber());
                    motivationReportModel.setBankBIK(active.getBikCode());
                    motivationReportModel.setBankName(active.getBankName());
                    motivationReportModel.setInn(active.getInn());
                    motivationReportModel.setRegistrationAddress(active.getRegistrationAddress());
                    motivationReportModel.setRegistrationIndex(active.getIndex());
                    motivationReportModel.setLatestAgreementUpdate(latestAgreementUpdate.get());
                    motivationReportModel.setLatestPassportUpdate(latestPassportUpdate.get());
                    motivationReportModel.setCorrectness(motivationStatusesMap.get(active.getMotivationCorrectStatus()));
                    motivationReportModel.setComment(active.getComment());
                    motivationReportModel.setMotivationStartDate(active.getStartDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    if (Objects.nonNull(orgUnit.getParent())) {
                        motivationReportModel.setOrgUnit(orgUnit.getParent().getName());
                    }
                    responseList.add(motivationReportModel);
                });
            }
        });
        return responseList;
    }

}
