package ru.softlab.efr.services.test.auth.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.exchange.model.LockEmployeeRq;
import ru.softlab.efr.services.auth.model.LockType;
import ru.softlab.efr.services.auth.model.UserLock;
import ru.softlab.efr.services.auth.services.UserLockService;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserLockRepository;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.test.auth.OperationLogServiceStatistics;
import ru.softlab.efr.services.test.auth.TestUtils;
import ru.softlab.efr.services.test.auth.config.TestRestApplicationConfig;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestRestApplicationConfig.class)
public class UserLockControllerTest {

    private static final String POST_USER_LOCK = "/auth/v1/employees/{id}/lock";
    private static final String POST_USER_UNLOCK = "/auth/v1/employees/{id}/unlock";

    private static final PrincipalDataImpl ADMIN_PRINCIPAL_DATA;

    @Autowired
    private UserLockRepository userLockRepository;

    @Autowired
    private UserLockService userLockService;

    @Autowired
    private OperationLogServiceStatistics operationLogServiceStatistics;

    static {
        ADMIN_PRINCIPAL_DATA = new PrincipalDataImpl();
        ADMIN_PRINCIPAL_DATA.setId(1L);
        ADMIN_PRINCIPAL_DATA.setFirstName("Иван");
        ADMIN_PRINCIPAL_DATA.setMiddleName("Иванович");
        ADMIN_PRINCIPAL_DATA.setSecondName("Иванов");
        ADMIN_PRINCIPAL_DATA.setRights(Arrays.asList(
                Right.EDIT_ROLES,
                Right.VIEW_ROLES,
                Right.SET_ADMIN_ROLE,
                Right.EDIT_USERS,
                Right.EDIT_PRODUCT_SETTINGS,
                Right.EDIT_UNDERWRITING_COEFFICIENTS,
                Right.EDIT_UNDERWRITING_SUMS,
                Right.VIEW_CLIENT,
                Right.VIEW_CLIENT_CONTRACTS,
                Right.CREATE_CONTRACT,
                Right.DELETE_CONTRACT,
                Right.EDIT_CONTRACT,
                Right.VIEW_CONTRACT_LIST_VSP,
                Right.VIEW_CONTRACT_LIST_OWNER,
                Right.VIEW_CONTRACT_REPORT_RF_VSP,
                Right.VIEW_CONTRACT_REPORT_VSP,
                Right.VIEW_CONTRACT_REPORT_OWNER,
                Right.VIEW_CONTRACT_REQUIRED_UNDERWRITING,
                Right.CHANGE_STATE_CONTRACT_REQUIRED_UNDERWRITING,
                Right.EDIT_CONTRACT_REQUIRED_UNDERWRITING
        ));
        ADMIN_PRINCIPAL_DATA.setBranch("1000001");
        ADMIN_PRINCIPAL_DATA.setOffice("2000001");
        ADMIN_PRINCIPAL_DATA.setPersonnelNumber("1-009");
    }

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    private MockMvc mockMvc;


    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_user_locked_by_hand.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void lockUserSuccess() throws Exception {
        operationLogServiceStatistics.reset();

        Long employeeId = 3L;
        LockEmployeeRq lockEmployeeRq = new LockEmployeeRq();
        lockEmployeeRq.setDescription("По просьбе службы безопасности");
        mockMvc.perform(post(POST_USER_LOCK, employeeId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(lockEmployeeRq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
        //UserLock lockInfo = userLockRepository.findFirstByUser_IdAndLocked(employeeId, true);
        List<UserLock> allLocks = userLockRepository.findAllByUserIdAndLocked(employeeId, true);
        assertTrue(!CollectionUtils.isEmpty(allLocks));

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("LOCK_EMPLOYEE", logs.get(0).getOperationKey());
        assertEquals(3L, logs.get(0).getOperationParameters().get("userId"));
        assertEquals("BY_HAND", logs.get(0).getOperationParameters().get("lockType"));
        assertEquals("По просьбе службы безопасности", logs.get(0).getOperationParameters().get("description"));
        assertEquals(OperationState.SUCCESS, logs.get(0).getOperationState());
    }

    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_user_locked_by_hand.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void lockUserFailUserAlreadyLocked() throws Exception {
        operationLogServiceStatistics.reset();

        Long employeeId = 3L;
        LockEmployeeRq lockEmployeeRq = new LockEmployeeRq();
        lockEmployeeRq.setDescription("По просьбе службы безопасности");
        mockMvc.perform(post(POST_USER_LOCK, employeeId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(lockEmployeeRq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("LOCK_EMPLOYEE", logs.get(0).getOperationKey());
        assertEquals(3L, logs.get(0).getOperationParameters().get("userId"));
        assertEquals("BY_HAND", logs.get(0).getOperationParameters().get("lockType"));
        assertEquals("По просьбе службы безопасности", logs.get(0).getOperationParameters().get("description"));
        assertEquals(OperationState.SUCCESS, logs.get(0).getOperationState());

        operationLogServiceStatistics.reset();

        mockMvc.perform(post(POST_USER_LOCK, employeeId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(lockEmployeeRq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].errorMessage", is("Пользователь с id=3 уже заблокирован")));

        logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("LOCK_EMPLOYEE", logs.get(0).getOperationKey());
        assertEquals(3L, logs.get(0).getOperationParameters().get("userId"));
        assertEquals("BY_HAND", logs.get(0).getOperationParameters().get("lockType"));
        assertEquals(OperationState.CLIENT_ERROR, logs.get(0).getOperationState());
    }


    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_user_locked_by_hand.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void unlockUserFailUserNotLocked() throws Exception {
        operationLogServiceStatistics.reset();

        Long employeeId = 3L;
        mockMvc.perform(post(POST_USER_UNLOCK, employeeId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(ADMIN_PRINCIPAL_DATA)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].errorMessage", is(String.format("Пользователь с id=%s не заблокирован или не существует", employeeId))));

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("UNLOCK_EMPLOYEE", logs.get(0).getOperationKey());
        assertEquals(3L, logs.get(0).getOperationParameters().get("userId"));
        assertEquals(OperationState.CLIENT_ERROR, logs.get(0).getOperationState());
    }


    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_user_locked_by_hand.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void unlockUserSuccess() throws Exception {
        operationLogServiceStatistics.reset();

        Long employeeId = 4L;
        mockMvc.perform(post(POST_USER_UNLOCK, employeeId)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(ADMIN_PRINCIPAL_DATA)))
                .andDo(print())
                .andExpect(status().isOk());

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("UNLOCK_EMPLOYEE", logs.get(0).getOperationKey());
        assertEquals(4L, logs.get(0).getOperationParameters().get("userId"));
        assertEquals(OperationState.SUCCESS, logs.get(0).getOperationState());
    }

    @Test
    @Sql(value = {"classpath:create_test_data.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void lockUserSuccessBySystemWithEndDateTimeAfterNow() throws Exception {
        operationLogServiceStatistics.reset();

        Long employeeId = 3L;
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(10L);
        userLockService.lock(employeeId, null, LockType.AUTO, endDateTime);
        assertTrue(userLockService.isLocked(employeeId));

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("LOCK_EMPLOYEE", logs.get(0).getOperationKey());
        assertEquals(3L, logs.get(0).getOperationParameters().get("userId"));
        assertEquals("AUTO", logs.get(0).getOperationParameters().get("lockType"));
        assertNull(logs.get(0).getOperationParameters().get("description"));
        assertEquals(OperationState.SUCCESS, logs.get(0).getOperationState());
    }

    @Test
    @Sql(value = {"classpath:create_test_data.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void lockUserSuccessBySystemWithEndDateTimeBeforeNow() throws Exception {
        operationLogServiceStatistics.reset();

        Long employeeId = 3L;
        LocalDateTime endDateTime = LocalDateTime.now().minusDays(10L);
        userLockService.lock(employeeId, null, LockType.AUTO, endDateTime);
        assertFalse(userLockService.isLocked(employeeId));

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("LOCK_EMPLOYEE", logs.get(0).getOperationKey());
        assertEquals(3L, logs.get(0).getOperationParameters().get("userId"));
        assertEquals("AUTO", logs.get(0).getOperationParameters().get("lockType"));
        assertNull(logs.get(0).getOperationParameters().get("description"));
        assertEquals(OperationState.SUCCESS, logs.get(0).getOperationState());
    }

    @Test
    @Sql(value = {"classpath:create_test_data.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void lockUserSuccessByHandWhenWasLockedBySystemBefore() throws Exception {
        operationLogServiceStatistics.reset();

        Long employeeId = 3L;
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(10L);
        userLockService.lock(employeeId, null, LockType.AUTO, endDateTime);
        userLockService.lock(employeeId, null, LockType.BY_HAND);
        assertTrue(userLockService.isLocked(employeeId));
        assertEquals(2, userLockRepository.findAllByUserIdAndLocked(employeeId, true).size());

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(2, logs.size());
        assertEquals("LOCK_EMPLOYEE", logs.get(0).getOperationKey());
        assertEquals(3L, logs.get(0).getOperationParameters().get("userId"));
        assertEquals("AUTO", logs.get(0).getOperationParameters().get("lockType"));
        assertNull(logs.get(0).getOperationParameters().get("description"));
        assertEquals("LOCK_EMPLOYEE", logs.get(1).getOperationKey());
        assertEquals(3L, logs.get(1).getOperationParameters().get("userId"));
        assertEquals("BY_HAND", logs.get(1).getOperationParameters().get("lockType"));
        assertNull(logs.get(1).getOperationParameters().get("description"));
        assertEquals(OperationState.SUCCESS, logs.get(1).getOperationState());
    }

}
