package ru.softlab.efr.services.test.auth.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.services.UserLoginAttemptsService;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserRepository;
import ru.softlab.efr.services.test.auth.config.TestRestApplicationConfig;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestRestApplicationConfig.class)
public class UserLoginAttemptsServiceTest {

    private static Long testUserId = 3L;

    private UserLoginAttemptsService userLoginAttemptsService;
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserLoginAttemptsService(UserLoginAttemptsService userLoginAttemptsService) {
        this.userLoginAttemptsService = userLoginAttemptsService;
    }

    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_user_locked_by_hand.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void loginAttemptsEqualMaxInDedicatedPeriod() {
        Employee user = userRepository.findOne(testUserId);
        userLoginAttemptsService.saveLoginAttempts(user);
        assertFalse(userLoginAttemptsService.isMaxAttemptExceeded(user, 1, 1000));
    }

    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_user_locked_by_hand.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void loginAttemptsMoreMaxInDedicatedPeriod() {
        Employee user = userRepository.findOne(testUserId);
        userLoginAttemptsService.saveLoginAttempts(user);
        userLoginAttemptsService.saveLoginAttempts(user);
        assertTrue(userLoginAttemptsService.isMaxAttemptExceeded(user, 1, 1000));
    }

    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_user_locked_by_hand.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void loginAttemptsMoreMaxOutDedicatedPeriod() {
        Employee user = userRepository.findOne(testUserId);
        userLoginAttemptsService.saveLoginAttempts(user);
        userLoginAttemptsService.saveLoginAttempts(user);
        assertFalse(userLoginAttemptsService.isMaxAttemptExceeded(user, 1, 0));
    }

    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_user_locked_by_hand.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void loginAttemptsMoreMaxWithEndLessPeriod() {
        Employee user = userRepository.findOne(testUserId);
        userLoginAttemptsService.saveLoginAttempts(user);
        userLoginAttemptsService.saveLoginAttempts(user);
        assertTrue(userLoginAttemptsService.isMaxAttemptExceeded(user, 1, -1));
    }

}
