package ru.softlab.efr.services.test.auth.integration;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.SessionsManageAuthServiceClient;
import ru.softlab.efr.services.auth.UserData;
import ru.softlab.efr.services.auth.exceptions.*;
import ru.softlab.efr.services.auth.exchange.CreateSessionRq;
import ru.softlab.efr.services.auth.exchange.CreateSessionRs;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.Role;
import ru.softlab.efr.services.auth.model.Session;
import ru.softlab.efr.services.auth.services.RoleStoreService;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.SessionRepository;
import ru.softlab.efr.services.test.auth.config.TestApplicationConfig;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static ru.softlab.efr.services.test.auth.integration.EmployeeIntegrationTestData.*;

/**
 * @author niculichev
 * @ created 28.04.2017
 * @ $Author$
 * @ $Revision$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@Ignore
public class SessionManageClientIntegrationTest extends IntegrationTestBase{
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionsManageAuthServiceClient sessionsClient;

    @Autowired
    private RoleStoreService roleStoreService;

    @After
    @SuppressWarnings("Duplicates")
    public void after(){
        sessionRepository.delete(sessionRepository.findAll().stream().filter(session -> session.getOwnerLogin().equals(CORRECT_EMPLOYEE.getLogin())).collect(Collectors.toSet()));
    }

    private CreateSessionRq createSessionRq(String login, String password){
        CreateSessionRq rq = new CreateSessionRq();
        rq.setLogin(login);
        rq.setPassword(password);
        return rq;
    }


    private List<Right> getRights(Employee employee) {
        List<Role> roles = employee.getRoles();
        Set<Right> rights = new HashSet<>();
        for (Role role : roles) {
            rights.addAll(role.getRights());
        }
        return new ArrayList<>(rights);
    }


    // *************************** ТЕСТЫ *****************************************

    @Test
    public void should_Success_When_CorrectCredentials() throws Exception {
        CreateSessionRs rs = sessionsClient.postSession(createSessionRq(CORRECT_EMPLOYEE.getLogin(), USERS_CORRECT_PASSWORD), TIMEOUT);
        assertNotNull(rs.getId());
        UserData user = rs.getUser();
        assertNotNull(user);

        assertEquals(CORRECT_EMPLOYEE.getLogin(), user.getLogin());
        assertEquals(CORRECT_EMPLOYEE.getFirstName(), user.getFirstName());
        assertEquals(CORRECT_EMPLOYEE.getSecondName(), user.getSecondName());
        assertEquals(CORRECT_EMPLOYEE.getMiddleName(), user.getMiddleName());
        assertEquals(CORRECT_EMPLOYEE.getMobilePhone(), user.getMobilePhone());
        assertEquals(CORRECT_EMPLOYEE.getEmail(), user.getEmail());
        assertEquals(CORRECT_EMPLOYEE.getPosition(), user.getPosition());
        assertEquals(CORRECT_EMPLOYEE.getOrgUnits().iterator().next().getName(), user.getOffice());
        assertEquals(CORRECT_EMPLOYEE.getOrgUnits().iterator().next().getParent().getName(), user.getBranch());
        assertEquals(CORRECT_EMPLOYEE.getPersonnelNumber(), user.getPersonnelNumber());
        assertThat(getRights(CORRECT_EMPLOYEE), containsInAnyOrder(user.getRights().toArray()));
    }

    @Test
    public void should_FindSession_When_CorrectCredentials() throws Exception {
        CreateSessionRs rs = sessionsClient.postSession(createSessionRq(CORRECT_EMPLOYEE.getLogin(), USERS_CORRECT_PASSWORD), TIMEOUT);
        assertNotNull(rs.getId());
        Session session = sessionRepository.findOne(rs.getId());
        assertNotNull(session);

        assertEquals(CORRECT_EMPLOYEE.getLogin(), session.getOwnerLogin());
        assertEquals(CORRECT_EMPLOYEE.getFirstName(), session.getOwnerFirstName());
        assertEquals(CORRECT_EMPLOYEE.getSecondName(), session.getOwnerSecondName());
        assertEquals(CORRECT_EMPLOYEE.getMiddleName(), session.getOwnerMiddleName());
        assertEquals(CORRECT_EMPLOYEE.getMobilePhone(), session.getOwnerMobilePhone());
        assertEquals(CORRECT_EMPLOYEE.getEmail(), session.getOwnerEmail());
        assertEquals(CORRECT_EMPLOYEE.getPosition(), session.getOwnerPosition());
        assertEquals(CORRECT_EMPLOYEE.getOrgUnits().iterator().next().getName(), session.getOwnerOffice());
        assertEquals(CORRECT_EMPLOYEE.getOrgUnits().iterator().next().getParent().getName(), session.getOwnerBranch());
        assertEquals(CORRECT_EMPLOYEE.getPersonnelNumber(), session.getOwnerPersonnelNumber());
        assertThat(getRights(CORRECT_EMPLOYEE), containsInAnyOrder(session.getOwnerRights().toArray()));
    }

    @Test(expected = UserAuthenticationException.class)
    public void should_UserAuthExc_When_IncorrectLogin() throws Exception {
        sessionsClient.postSession(createSessionRq("helloWorld", USERS_CORRECT_PASSWORD), TIMEOUT);
    }

    @Test(expected = UserAuthenticationException.class)
    public void should_UserAuthExc_When_UnregisteredLogin() throws Exception {
        sessionsClient.postSession(createSessionRq(DOMAIN_USER_WRONG_LOGIN, USERS_CORRECT_PASSWORD), TIMEOUT);
    }

    @Test(expected = UserAuthenticationException.class)
    public void should_UserAuthExc_When_IncorrectPassword() throws Exception {
        sessionsClient.postSession(createSessionRq(CORRECT_EMPLOYEE.getLogin(), USERS_WRONG_PASSWORD), TIMEOUT);
    }

    @Test(expected = UserOfficeNotFoundException.class)
    public void should_UserOfficeNotFoundExc_When_IncorrectUserOffice() throws Exception {
        sessionsClient.postSession(createSessionRq(USER_WRONG_DEPARTMENT_LOGIN, USERS_CORRECT_PASSWORD), TIMEOUT);
    }

    @Test(expected = UserRoleNotFoundException.class)
    public void should_UserRoleNotFoundExc_When_IncorrectUserRoles() throws Exception {
        sessionsClient.postSession(createSessionRq(USER_WRONG_ROLE_LOGIN, USERS_CORRECT_PASSWORD), TIMEOUT);
    }

    @Test(expected = LoginBlockException.class)
    public void should_LoginBlockExc_When_IncorrectUserRoles() throws Exception {
        sessionsClient.postSession(createSessionRq(USER_BLOCKED_LOGIN, USERS_CORRECT_PASSWORD), TIMEOUT);
    }

    @Test(expected = UserWithoutRoleException.class)
    public void should_UserWithoutRoleExc_When_EmptyUserRoles() throws Exception {
        sessionsClient.postSession(createSessionRq(USER_EMPTY_ROLE_LOGIN, USERS_CORRECT_PASSWORD), TIMEOUT);
    }

    @Test
    public void should_SuccessCloseSession_When_CorrectUID() throws Exception {
        Session session = new Session();
        session.setCreationDate(new Date());
        session.setState(Session.State.active);
        session.setOwnerLogin(CORRECT_EMPLOYEE.getLogin());
        session.setOwnerFirstName(CORRECT_EMPLOYEE.getFirstName());
        session.setOwnerSecondName(CORRECT_EMPLOYEE.getSecondName());
        session.setOwnerMiddleName(CORRECT_EMPLOYEE.getMiddleName());
        session.setOwnerMobilePhone(CORRECT_EMPLOYEE.getMobilePhone());
        session.setOwnerEmail(CORRECT_EMPLOYEE.getEmail());
        session.setOwnerPosition(CORRECT_EMPLOYEE.getPosition());
        session.setOwnerOffice(CORRECT_EMPLOYEE.getOrgUnits().iterator().next().getName());
        session.setOwnerBranch(CORRECT_EMPLOYEE.getOrgUnits().iterator().next().getParent().getName());
        session.setOwnerPersonnelNumber(CORRECT_EMPLOYEE.getPersonnelNumber());
        session.setOwnerRights(Arrays.asList(Right.values()));
        sessionRepository.save(session);

        sessionsClient.deleteSession(session.getUid(), TIMEOUT);
        Session changedSession = sessionRepository.findOne(session.getUid());
        assertNotNull(changedSession);
        assertEquals(changedSession.getState(), Session.State.closed);
    }

    @Test(expected = EntityNotFoundException.class)
    public void should_NotFoundCloseSession_When_IncorrectUID() throws Exception {
        sessionsClient.deleteSession("notExistingUID", TIMEOUT);
    }
}
