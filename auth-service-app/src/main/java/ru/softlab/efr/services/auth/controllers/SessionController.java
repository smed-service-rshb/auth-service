package ru.softlab.efr.services.auth.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.model.OperationMode;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.efr.infrastructure.logging.api.services.OperationLogService;
import ru.softlab.efr.services.auth.exceptions.*;
import ru.softlab.efr.services.auth.exchange.BadEntityRs;
import ru.softlab.efr.services.auth.exchange.model.CreateSessionRq;
import ru.softlab.efr.services.auth.exchange.model.CreateSessionRs;
import ru.softlab.efr.services.auth.model.Session;
import ru.softlab.efr.services.auth.services.AuthenticateService;
import ru.softlab.efr.services.auth.services.SessionStoreService;

import javax.validation.Valid;
import java.util.Calendar;

import static ru.softlab.efr.services.auth.controllers.Utils.buildErrorRes;

/**
 * Контроллер обработки запросов, связанных с сессиями
 *
 * @author niculichev
 * @since 12.04.2017
 */
@Validated
@RestController
public class SessionController extends BaseAuthController implements SessionApi {
    private static final Logger LOGGER = Logger.getLogger(SessionController.class);
    private static final String LOG_ERROR_PREFIX = "Ошибка аутентификации пользователя ";
    private static final String USER_LOGIN_FIELD = "login";

    private SessionStoreService sessionStoreService;
    private OperationLogService operationLogService;

    @Autowired
    public SessionController(
            @Qualifier("sessionServiceDaoImpl") SessionStoreService sessionStoreService,
            OperationLogService operationLogService,
            AuthenticateService authenticateService) {
        super(operationLogService, authenticateService);
        this.sessionStoreService = sessionStoreService;
        this.operationLogService = operationLogService;
    }

    @Override
    public ResponseEntity<Void> closeSession(@PathVariable String id) throws Exception {
        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("CLOSING_SESSION");
        operationLogEntry.setOperationDescription("Закрытие сессии пользователя");
        operationLogEntry.setOperationParameter("sessionId", id);
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);

        try {
            // не смогли обновить не одной сессии
            if (sessionStoreService.close(id) == 0) {
                String errorMessage = "Сессия не найдена по идентификатору " + id;
                LOGGER.warn(errorMessage);
                operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
                operationLogEntry.setOperationParameter("error", errorMessage);
                return ResponseEntity.notFound().build();
            }

            operationLogEntry.setOperationState(OperationState.SUCCESS);
            return ResponseEntity.ok().build();
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);
        }
    }

    @Override
    public ResponseEntity<Void> updateSession(@PathVariable("id") String id, @Valid @RequestParam String officeName,
                                              @Valid @RequestParam String branchName) {
        Session session = sessionStoreService.getByUID(id);
        if (session == null) {
            LOGGER.warn(String.format("Сессия не найдена по идентификатору %s.", id));
            return ResponseEntity.notFound().build();
        }
        session.setOwnerBranch(branchName);
        session.setOwnerOffice(officeName);
        sessionStoreService.save(session);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CreateSessionRs> authenticate(@Valid @RequestBody CreateSessionRq createSessionRq) throws Exception {
        return standardAuth(createSessionRq.getLogin(), createSessionRq.getPassword(), null);
    }

    @ExceptionHandler
    ResponseEntity handleException(final UserOfficeNotFoundException exception) {
        LOGGER.error("Ошибка обработки запроса: офис не найден", exception);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(BadEntityRs.userOfficeNotFoundResponse(exception));
    }

    @ExceptionHandler
    ResponseEntity handleException(final LoginBlockException exception) {
        LOGGER.error("Пользователь заблокирован.", exception);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(BadEntityRs.userBlockedResponse(exception));
    }

    @ExceptionHandler
    ResponseEntity handleException(final UserIdentificationException exception) {
        LOGGER.error(LOG_ERROR_PREFIX, exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildErrorRes(USER_LOGIN_FIELD, exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity handleException(final UserAuthenticationException exception) {
        LOGGER.error(LOG_ERROR_PREFIX, exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildErrorRes(USER_LOGIN_FIELD, exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity handleException(final UserRoleNotFoundException exception) {
        LOGGER.error(LOG_ERROR_PREFIX, exception);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(buildErrorRes(USER_LOGIN_FIELD, exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity handleException(final UserWithoutRoleException exception) {
        LOGGER.error(LOG_ERROR_PREFIX, exception);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(buildErrorRes(USER_LOGIN_FIELD, exception.getMessage()));
    }


}
