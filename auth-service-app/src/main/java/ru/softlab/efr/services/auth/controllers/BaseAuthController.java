package ru.softlab.efr.services.auth.controllers;

import org.springframework.http.ResponseEntity;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.model.OperationMode;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.efr.infrastructure.logging.api.services.OperationLogService;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.exchange.model.CreateSessionRs;
import ru.softlab.efr.services.auth.exchange.model.CreateSessionRsUser;
import ru.softlab.efr.services.auth.exchange.model.OrgUnitData;
import ru.softlab.efr.services.auth.model.EmployeeGroup;
import ru.softlab.efr.services.auth.model.Session;
import ru.softlab.efr.services.auth.model.SessionData;
import ru.softlab.efr.services.auth.services.AuthenticateService;

import java.util.Calendar;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Базовый класс для контроллеров, реализующий процесс аутентифкации и авторизации.
 *
 * @author danilov
 * @since 28.02.2019
 */
abstract class BaseAuthController {
    private OperationLogService operationLogService;
    private AuthenticateService authenticateService;

    BaseAuthController(OperationLogService operationLogService, AuthenticateService authenticateService) {
        this.operationLogService = operationLogService;
        this.authenticateService = authenticateService;
    }

    ResponseEntity<CreateSessionRs> standardAuth(String login, String password, String passwordHash) throws Exception {
        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("USER_AUTHENTICATION");
        operationLogEntry.setOperationDescription("Аутентификация пользователя");
        operationLogEntry.setOperationParameter("login", login);
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);
        try {
            SessionData session;
            if (Objects.isNull(passwordHash)) {
                session = authenticateService.authenticate(login, password);
            } else {
                session = authenticateService.authenticateByPasswordHash(login, passwordHash);
            }
            operationLogEntry.setOperationState(OperationState.SUCCESS);
            return ResponseEntity.ok().body(new CreateSessionRs(session.getSession().getUid(), buildEntityInfo(session)));
        } catch (Exception ex) {
            operationLogEntry.setOperationState(OperationState.SYSTEM_ERROR);
            operationLogEntry.setOperationParameter("error", ex.getMessage());
            throw ex;
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);
        }
    }

    private CreateSessionRsUser buildEntityInfo(SessionData sessionData) {
        CreateSessionRsUser rs = new CreateSessionRsUser();
        Session session = sessionData.getSession();
        rs.setId(session.getOwnerId());
        rs.setLogin(session.getOwnerLogin());
        rs.setFirstName(session.getOwnerFirstName());
        rs.setSecondName(session.getOwnerSecondName());
        rs.setMiddleName(session.getOwnerMiddleName());
        rs.setMobilePhone(session.getOwnerMobilePhone());
        rs.setEmail(session.getOwnerEmail());
        rs.setPosition(session.getOwnerPosition());
        rs.setOffice(session.getOwnerOffice());
        rs.setBranch(session.getOwnerBranch());
        rs.setOffices(sessionData.getEmployee().getOrgUnits().stream()
                .map(orgUnit -> {
                    OrgUnitData data = new OrgUnitData();
                    data.setId(orgUnit.getId());
                    data.setOffice(orgUnit.getName());
                    data.setBranch(orgUnit.getParent() != null ? orgUnit.getParent().getName() : null);
                    return data;
                }).collect(Collectors.toList()));
        rs.setPersonnelNumber(session.getOwnerPersonnelNumber());
        rs.setRights(session.getOwnerRights().parallelStream().map(Right::toString).collect(Collectors.toList()));
        rs.setChangePassword(session.getChangePassword());
        rs.setGroupCodes(sessionData.getEmployee().getGroups().stream().map(EmployeeGroup::getCode).collect(Collectors.toList()));
        return rs;
    }
}
