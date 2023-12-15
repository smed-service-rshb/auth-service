package ru.softlab.efr.services.auth.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.exchange.model.CheckModel;
import ru.softlab.efr.services.auth.exchange.model.ErrorModel;
import ru.softlab.efr.services.auth.exchange.model.MotivationSettings;
import ru.softlab.efr.services.auth.model.Session;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.SessionRepository;

import java.time.*;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class MotivationTimeControlService {

    private static final Logger LOGGER = Logger.getLogger(MotivationTimeControlService.class);

    private SessionRepository sessionRepository;
    private MotivationSettingsService motivationSettingsService;

    @Autowired
    public MotivationTimeControlService(
            SessionRepository sessionRepository,
            MotivationSettingsService motivationSettingsService) {
        this.sessionRepository = sessionRepository;
        this.motivationSettingsService = motivationSettingsService;
    }

    public ErrorModel getMotivationStartDate(Long ownerId, LocalDate startLocalDate) {
        ErrorModel errorModel = new ErrorModel();
        CheckModel checkModel = new CheckModel();
        checkModel.setKey("Проверка допустимости выбранной даты начала мотивации");
        Date startDate = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Long firstLoginTimestamp = getFirstLoginTimestamp(ownerId);
        long currentTimestamp = new Date().getTime();
        if (Objects.isNull(firstLoginTimestamp)) {
            checkModel.setValue("пользователь еще не авторизировался");
            errorModel.addErrorsItem(checkModel);
            LOGGER.info("пользователь еще не авторизировался");
            return errorModel;
        } else {
            firstLoginTimestamp = atStartOfDay(new Date(firstLoginTimestamp)).getTime();
        }
        if (startDate.getTime() < firstLoginTimestamp) {
            checkModel.setValue("Недопустимо указывать дату начала мотивации меньше даты первой авторизации в систему");
            errorModel.addErrorsItem(checkModel);
            LOGGER.info("Недопустимо указывать дату начала мотивации меньше даты первой авторизации в систему");
        }
        if (startDate.getTime() > currentTimestamp) {
            checkModel.setValue("Недопустимо указывать дату начала мотивации больше текущей даты");
            errorModel.addErrorsItem(checkModel);
            LOGGER.info("Недопустимо указывать дату начала мотивации больше текущей даты");
        }
        return errorModel;
    }

    public LocalDate getMotivationEndDate(LocalDate startLocalDate) {
        MotivationSettings motivationSettings = motivationSettingsService.getCurrentSettings();
        return startLocalDate.plusMonths(motivationSettings.getExpireTime());
    }

    private Long getFirstLoginTimestamp(Long ownerId) {
        Session session = Optional.ofNullable(sessionRepository.getFirstByOwnerIdOrderByCreationDate(ownerId)).orElse(new Session());
        if (Objects.nonNull(session.getCreationDate())) {
            LocalDate getLocal = convertToLocalDateViaMilisecond(session.getCreationDate());
            return Date.from(getLocal.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime();
        }
        return null;
    }

    public boolean checkMotivationActiveStatus(LocalDate endDateLocal) {
        return (LocalDate.now().isBefore(endDateLocal) || LocalDate.now().equals(endDateLocal));
    }

    private LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
