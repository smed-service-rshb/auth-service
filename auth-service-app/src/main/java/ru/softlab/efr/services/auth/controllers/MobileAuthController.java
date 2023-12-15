package ru.softlab.efr.services.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.efr.infrastructure.logging.api.services.OperationLogService;
import ru.softlab.efr.services.auth.exceptions.*;
import ru.softlab.efr.services.auth.exchange.model.CreateSessionRs;
import ru.softlab.efr.services.auth.exchange.model.MobileLoginRequest;
import ru.softlab.efr.services.auth.exchange.model.ShortCodeRequest;
import ru.softlab.efr.services.auth.exchange.model.ShortCodeResponse;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.Session;
import ru.softlab.efr.services.auth.services.AuthenticateService;
import ru.softlab.efr.services.auth.services.MobileAuthService;
import ru.softlab.efr.services.auth.services.SessionStoreService;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Objects;

/**
 * Контроллер обработки запросов, связанных с устройствами
 *
 * @author danilov
 * @since 26.02.2019
 */
@Validated
@RestController
public class MobileAuthController extends BaseAuthController implements MobileAuthServiceApi {
    private OperationLogService operationLogService;
    private MobileAuthService mobileAuthService;
    private SessionStoreService sessionStoreService;

    @Autowired
    public MobileAuthController(OperationLogService operationLogService,
                                MobileAuthService mobileAuthService,
                                AuthenticateService authenticateService,
                                SessionStoreService sessionStoreService) {
        super(operationLogService, authenticateService);
        this.operationLogService = operationLogService;
        this.mobileAuthService = mobileAuthService;
        this.sessionStoreService = sessionStoreService;
    }

    @Override
    public ResponseEntity<CreateSessionRs> mobileAuthService(@Valid @RequestBody MobileLoginRequest body) throws Exception {
        if (MobileLoginRequest.AuthMethodEnum.SHORT_CODE.equals(body.getAuthMethod())) {
            Employee user = mobileAuthService.getEmployerForRegisteredDevice(body.getLogin(), body.getPassword());
            if (Objects.isNull(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return standardAuth(user.getLogin(), null, user.getPasswordHash());
        } else {
            return standardAuth(body.getLogin(), body.getPassword(), null);
        }
    }

    @Override
    public ResponseEntity<ShortCodeResponse> mobileAuthShortCodeService(@Valid @RequestBody ShortCodeRequest body) {
        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("USER_DEVICE_REGISTRATION");
        operationLogEntry.setOperationDescription("Регистрация устройства пользователя");
        operationLogEntry.setOperationParameter("deviceCode", body.getIdentifierDevice());
        try {
            Session sessionData = sessionStoreService.getByUID(body.getSessionId());
            if (Session.State.active.equals(sessionData.getState())) {
                String responseCode = mobileAuthService.registerNewDevice(sessionData, body);
                ShortCodeResponse shortCodeResponse = new ShortCodeResponse();
                shortCodeResponse.setToken(responseCode);
                return ResponseEntity.ok(shortCodeResponse);
            } else {
                operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
                operationLogEntry.setOperationParameter("error", "Пользовательская сессия закрыта");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception ex) {
            operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
            operationLogEntry.setOperationParameter("error", ex.getMessage());
            throw ex;
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);
        }
    }

    @ExceptionHandler
    private ResponseEntity handleException(final UserAuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler
    ResponseEntity handleException(final LoginBlockException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @ExceptionHandler
    ResponseEntity handleException(final UserIdentificationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler
    ResponseEntity handleException(final UserRoleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @ExceptionHandler
    ResponseEntity handleException(final UserWithoutRoleException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }
}
