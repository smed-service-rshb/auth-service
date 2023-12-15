package ru.softlab.efr.services.test.auth.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.softlab.efr.services.auth.exceptions.LoginBlockException;
import ru.softlab.efr.services.auth.exceptions.UserAuthenticationException;
import ru.softlab.efr.services.auth.exceptions.UserIdentificationException;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.services.AuthenticateService;
import ru.softlab.efr.services.auth.services.UserLoginAttemptsService;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserLoginAttemptsRepository;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserRepository;
import ru.softlab.efr.services.test.auth.config.TestRestApplicationConfig;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestRestApplicationConfig.class)
public class AuthenticateServiceTest {

    private static Long testUserId = 3L;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private AuthenticateService authenticateService;
    private UserLoginAttemptsService userLoginAttemptsService;
    private UserRepository userRepository;
    private UserLoginAttemptsRepository userLoginAttemptsRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAuthenticateService(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @Autowired
    public void setUserLoginAttemptsService(UserLoginAttemptsService userLoginAttemptsService) {
        this.userLoginAttemptsService = userLoginAttemptsService;
    }

    @Autowired
    public void setUserLoginAttemptsRepository(UserLoginAttemptsRepository userLoginAttemptsRepository) {
        this.userLoginAttemptsRepository = userLoginAttemptsRepository;
    }

    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_set_user_login_attempt_settings.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void lockUserBecauseAttemptsExceeded() throws UserAuthenticationException, LoginBlockException, UserIdentificationException {
        Employee user = userRepository.findOne(testUserId);
        userLoginAttemptsService.saveLoginAttempts(user);
        exception.expect(UserAuthenticationException.class);
        authenticateService.auth(user.getLogin(), "wrongPassword", null);
    }


    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_set_user_login_attempt_settings.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAttemptIsSavedAfterFail() throws LoginBlockException, UserIdentificationException {
        Employee user = userRepository.findOne(testUserId);
        userLoginAttemptsService.saveLoginAttempts(user);
        try {
            authenticateService.auth(user.getLogin(), "wrongPassword", null);
        } catch (UserAuthenticationException e) {
            assertEquals(2, userLoginAttemptsRepository.countAllByUser(user).intValue());
        }
    }

}
