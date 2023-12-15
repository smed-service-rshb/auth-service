package ru.softlab.efr.services.test.auth.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.common.utilities.rest.RestPageImpl;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.efr.services.auth.exchange.ChangePasswordRq;
import ru.softlab.efr.services.auth.exchange.model.*;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserRepository;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.test.auth.OperationLogServiceStatistics;
import ru.softlab.efr.services.test.auth.TestUtils;
import ru.softlab.efr.services.test.auth.config.TestRestApplicationConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.test.auth.TestData.*;
import static ru.softlab.efr.services.test.auth.integration.EmployeeIntegrationTestData.CORRECT_EMPLOYEE;

/**
 * @author niculichev
 * @ created 30.04.2017
 * @ $Author$
 * @ $Revision$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestRestApplicationConfig.class)
public class EmployeeControllerAndServiceTest {

    private static final Sort USER_LIST_SORT_BY_NAME = new Sort(Sort.Direction.ASC, "secondName", "firstName", "middleName");

    private static final String PUT_EMPLOYEES_URL = "/auth/v1/employees";
    private static final String POST_CREATE_EMPLOYEE_URL = "/auth/v1/employees";
    private static final String POST_UPDATE_EMPLOYEE_URL = "/auth/v1/employees/%s";
    private static final String GET_EMPLOYEE_URL = "/auth/v1/employees/%s";
    private static final String GET_EMPLOYEE_FOR_EXTRACT_URL = "/auth/v1/private/employees/all";
    private static final String POST_CHANGE_PASSWORD_URL = "/auth/v1/changePassword";
    private static final String GET_RESET_PASSWORD_URL = "/auth/v1/resetPassword/{id}";
    private static final String GET_RESET_CLIENT_PASSWORD_URL = "/auth/v1/private/client/resetPassword/%s?password=%s";
    private static final String POST_USER_CHANGE_LOGIN = "/auth/v1/changeLogin";

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperationLogServiceStatistics operationLogServiceStatistics;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    private ParameterizedTypeReference<RestPageImpl<EmployeeDataForList>> restPage() {
        return new ParameterizedTypeReference<RestPageImpl<EmployeeDataForList>>() {

        };
    }

    /**
     * Тест сервиса POST /auth/v1/employees (создание новой учетной записи пользователя)
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createEmployeeSuccess() throws Exception {
        CreateOrUpdateEmployeeRq rq = new CreateOrUpdateEmployeeRq();
        rq.setFirstName(CORRECT_EMPLOYEE.getFirstName());
        rq.setSecondName(CORRECT_EMPLOYEE.getSecondName());
        rq.setMiddleName(CORRECT_EMPLOYEE.getMiddleName());
        rq.setEmail(CORRECT_EMPLOYEE.getEmail());
        rq.setOrgUnits(Arrays.asList(CORRECT_EMPLOYEE.getOrgUnits().iterator().next().getId()));
        rq.setMobilePhone(CORRECT_EMPLOYEE.getMobilePhone());
        rq.setPassword("12345Abc");
        rq.setPersonnelNumber(CORRECT_EMPLOYEE.getPersonnelNumber());
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        rq.setRoles(roleIds);
        rq.setSegmentId(CORRECT_EMPLOYEE.getSegment().getId());
        rq.setGroupIds(Arrays.asList(1L, 2L));
        mockMvc.perform(post(POST_CREATE_EMPLOYEE_URL)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }

    /**
     * Тест сервиса POST /auth/v1/employees (создание новой учетной записи пользователя)
     * для проверки получения ошибки по причине того, что задан пароль не удовлетворяющий требованиям к сложности.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createEmployeeFailBecausePasswordIsWeak() throws Exception {
        CreateOrUpdateEmployeeRq rq = new CreateOrUpdateEmployeeRq();
        rq.setFirstName(CORRECT_EMPLOYEE.getFirstName());
        rq.setSecondName(CORRECT_EMPLOYEE.getSecondName());
        rq.setMiddleName(CORRECT_EMPLOYEE.getMiddleName());
        rq.setEmail(CORRECT_EMPLOYEE.getEmail());
        rq.setOrgUnits(Arrays.asList(CORRECT_EMPLOYEE.getOrgUnits().iterator().next().getId()));
        rq.setMobilePhone(CORRECT_EMPLOYEE.getMobilePhone());
        rq.setPassword("1");
        rq.setPersonnelNumber(CORRECT_EMPLOYEE.getPersonnelNumber());
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        rq.setRoles(roleIds);
        rq.setSegmentId(CORRECT_EMPLOYEE.getSegment().getId());
        mockMvc.perform(post(POST_CREATE_EMPLOYEE_URL)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].errorMessage", containsString("Пароль должен иметь длину не менее 6 символов")));
    }

    /**
     * Тест сервиса GET /auth/v1/employees (получение списка пользователей)
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getEmployeesSuccess() throws Exception {

        Pageable pageable = new PageRequest(0, 10, USER_LIST_SORT_BY_NAME);

        mockMvc.perform(put(PUT_EMPLOYEES_URL).param("sort", "secondName")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].fullName", is("Антонов Антон Антонович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].login", is("vsp-head")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email", is("antonov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].mobilePhone", is("+79110000008")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].personnelNumber", is("10008")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].position", is("Руководитель ВСП")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branches[0]", is("ДРБ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].offices", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].segment", is("Массовый")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roles", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roles[0]", is("Руководитель ВСП")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].fullName", is("Иванов Иван Иванович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[3].fullName", is("Фёдоров Фёдор Фёдорович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lockStatus", is(false)));
    }

    /**
     * Тест сервиса PUT /auth/v1/employees (получение отфильтрованного списка пользователей)
     * для проверки получения корректного ответа если заданы не все параметры поиска.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getFiltratedEmployeesSuccess() throws Exception {

        FilterEmployeesRq filterData = new FilterEmployeesRq();
        filterData.setSecondName("Антонов");
        filterData.setMiddleName("Антонович");
        filterData.setFirstName("Антон");
        filterData.setPersonnelNumber("10008");

        mockMvc.perform(put(PUT_EMPLOYEES_URL)
                .param("sort", "secondName")
                .param("hasFilter", "true")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(filterData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].fullName", is("Антонов Антон Антонович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].login", is("vsp-head")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email", is("antonov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].mobilePhone", is("+79110000008")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].personnelNumber", is("10008")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].position", is("Руководитель ВСП")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branches[0]", is("ДРБ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].offices", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].segment", is("Массовый")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roles", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roles[0]", is("Руководитель ВСП")));
    }

    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getFiltratedEmployeesMotivationSuccess() throws Exception {

        FilterEmployeesRq filterData = new FilterEmployeesRq();
        filterData.setSecondName("Антонов");
        filterData.setMiddleName("Антонович");
        filterData.setFirstName("Антон");
        filterData.setPersonnelNumber("10008");
        filterData.setMotivationCorrectStatus(MotivationCorrectStatus.CORRECT);

        mockMvc.perform(put(PUT_EMPLOYEES_URL)
                .param("sort", "secondName")
                .param("hasFilter", "true")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(filterData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].fullName", is("Антонов Антон Антонович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].login", is("vsp-head")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email", is("antonov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].mobilePhone", is("+79110000008")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].personnelNumber", is("10008")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].position", is("Руководитель ВСП")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branches[0]", is("ДРБ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].offices", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].segment", is("Массовый")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roles", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roles[0]", is("Руководитель ВСП")));
    }

    /**
     * Тест сервиса PUT /auth/v1/employees (получение отфильтрованного списка пользователей)
     * для проверки получения корректного ответа если заданы все параметры поиска.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getFiltratedAllParameterEmployeesSuccess() throws Exception {

        FilterEmployeesRq filterData = new FilterEmployeesRq();
        filterData.setSecondName("ант");
        filterData.setMiddleName("Антонович");
        filterData.setFirstName("антон");
        filterData.setBranches(Arrays.asList("ДРБ"));
        filterData.setOrgUnitIdes(Arrays.asList(2000001L, 2000002L));
        filterData.setPersonnelNumber("10008");
        filterData.setPosition("руководитель");
        filterData.setRoleId(2L);

        mockMvc.perform(put(PUT_EMPLOYEES_URL)
                .param("sort", "secondName")
                .param("hasFilter", "true")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(filterData))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].fullName", is("Антонов Антон Антонович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].login", is("vsp-head")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email", is("antonov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].mobilePhone", is("+79110000008")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].personnelNumber", is("10008")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].position", is("Руководитель ВСП")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branches[0]", is("ДРБ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].offices", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].segment", is("Массовый")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roles", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roles[0]", is("Руководитель ВСП")));
    }

    /**
     * Тест сервиса GET /auth/v1/employees (получение списка пользователей c использованием фильтра)
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getEmployeesWithFilterSuccess() throws Exception {

        FilterEmployeesRq filterEmployeesRq = new FilterEmployeesRq();
        filterEmployeesRq.setSecondName("Антонов");
//        filterEmployeesRq.setOrgUnitId(2000001L);
        mockMvc.perform(put(PUT_EMPLOYEES_URL)
                .param("sort", "secondName")
                .param("order", "desc")
                .param("hasFilter", "true")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(restPage()))
                .content(TestUtils.convertObjectToJson(filterEmployeesRq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].fullName", is("Антонов Антон Антонович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].login", is("vsp-head")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email", is("antonov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].mobilePhone", is("+79110000008")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].personnelNumber", is("10008")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].position", is("Руководитель ВСП")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].branches[0]", is("ДРБ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].offices", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].segment", is("Массовый")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roles", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].roles[0]", is("Руководитель ВСП")));
    }

    /**
     * Тест сервиса GET /auth/v1/employees/{id} (получение информации об учетной записи сотрудника)
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getEmployeeByIdSuccess() throws Exception {
        mockMvc.perform(get(String.format(GET_EMPLOYEE_URL, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", is("chief-admin")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("Иван")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.secondName", is("Иванов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.middleName", is("Иванович")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mobilePhone", is("+79110000001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is("ivanov@example.org")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position", is("Главный администратор системы")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personnelNumber", is("10001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position", is("Главный администратор системы")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orgUnits[0]", is(2000001)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.segmentId", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles[0]", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupIds", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupIds[0]", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lockStatus", is(false)));
    }

    /**
     * Тест сервиса GET /auth/v1/private/employees/all
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getEmployeeWithDeletedSuccess() throws Exception {
        mockMvc.perform(get(GET_EMPLOYEE_FOR_EXTRACT_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }


    /**
     * Тест сервиса POST /auth/v1/employees (изменение данных учетной записи пользователя)
     * для проверки получения корректного ответа и корректности сохранения данных
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateEmployeeSuccess() throws Exception {
        operationLogServiceStatistics.reset();

        CreateOrUpdateEmployeeRq rq = new CreateOrUpdateEmployeeRq();
        rq.setFirstName("Петров");
        rq.setSecondName("Петр");
        rq.setMiddleName("Петрович");
        rq.setLogin("12345");
        rq.setOrgUnits(Arrays.asList(2000002L));
        rq.setMobilePhone("+71234561116");
        rq.setPersonnelNumber("12345");
        rq.setPosition("Директор");
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(2L);
        rq.setRoles(roleIds);
        rq.setSegmentId(2L);
        mockMvc.perform(put(String.format(POST_UPDATE_EMPLOYEE_URL, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        Employee employee = userRepository.findById(1L);
        assertEquals("Петров", employee.getFirstName());
        assertEquals("Петр", employee.getSecondName());
        assertEquals("Петрович", employee.getMiddleName());
        assertEquals("12345", employee.getLogin());
        assertEquals(new Long(2000002L), employee.getOrgUnits().iterator().next().getId());
        assertEquals("+71234561116", employee.getMobilePhone());
        assertEquals("12345", employee.getPersonnelNumber());
        assertEquals("Директор", employee.getPosition());
        assertEquals(1, employee.getRoles().size());
        assertEquals(new Long(2L), employee.getRoles().get(0).getId());
        assertEquals(new Long(2L), employee.getSegment().getId());

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("UPDATE_EMPLOYEE", logs.get(0).getOperationKey());
        assertNotNull(logs.get(0).getOperationParameters().get("newEmployeeData"));
        assertNotNull(logs.get(0).getOperationParameters().get("oldEmployeeData"));
        assertEquals(OperationState.SUCCESS, logs.get(0).getOperationState());
    }


    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateEmployeeFailBecauseExistsWithSameLogin() throws Exception {
        operationLogServiceStatistics.reset();

        CreateOrUpdateEmployeeRq rq = new CreateOrUpdateEmployeeRq();
        rq.setFirstName("Петров");
        rq.setSecondName("Петр");
        rq.setMiddleName("Петрович");
        rq.setLogin("vsp-head");
        rq.setEmail("test@email.ru");
        rq.setOrgUnits(Arrays.asList(2000002L));
        rq.setMobilePhone("+71234561116");
        rq.setPassword("123Abcdef");
        rq.setPersonnelNumber("vsp-head");
        rq.setPosition("Директор");
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(2L);
        rq.setRoles(roleIds);
        rq.setSegmentId(2L);
        mockMvc.perform(put(String.format(POST_UPDATE_EMPLOYEE_URL, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("UPDATE_EMPLOYEE", logs.get(0).getOperationKey());
        assertNotNull(logs.get(0).getOperationParameters().get("newEmployeeData"));
        assertNotNull(logs.get(0).getOperationParameters().get("oldEmployeeData"));
        assertEquals(OperationState.CLIENT_ERROR, logs.get(0).getOperationState());
    }

    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateEmployeeFailBecausePasswordIsWeak() throws Exception {
        CreateOrUpdateEmployeeRq rq = new CreateOrUpdateEmployeeRq();
        rq.setFirstName("Петров");
        rq.setSecondName("Петр");
        rq.setMiddleName("Петрович");
        rq.setLogin("12345");
        rq.setEmail("test@email.ru");
        rq.setOrgUnits(Arrays.asList(2000002L));
        rq.setMobilePhone("+71234561116");
        rq.setPassword("1");
        rq.setPersonnelNumber("vsp-head");
        rq.setPosition("Директор");
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(2L);
        rq.setRoles(roleIds);
        rq.setSegmentId(2L);
        mockMvc.perform(put(String.format(POST_UPDATE_EMPLOYEE_URL, "1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].errorMessage",
                        containsString("Пароль должен иметь длину не менее 6 символов")));
    }


    /**
     * Тест сервиса GET /auth/v1/employees/{id} (получение информации об учетной записи сотрудника)
     * для проверки получения отказа по причине отсутствия учётной записи с указанным идентификатором.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getEmployeeByIdNotFound() throws Exception {
        mockMvc.perform(get(String.format(GET_EMPLOYEE_URL, "-1"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createEmployeeFailBecauseRequiredFieldIsEmpty() throws Exception {
        CreateOrUpdateEmployeeRq rq = new CreateOrUpdateEmployeeRq();
        rq.setFirstName(CORRECT_EMPLOYEE.getFirstName());
        rq.setSecondName(CORRECT_EMPLOYEE.getSecondName());
        rq.setLogin(CORRECT_EMPLOYEE.getLogin());
        rq.setSegmentId(CORRECT_EMPLOYEE.getSegment().getId());
        rq.setPersonnelNumber(CORRECT_EMPLOYEE.getPersonnelNumber());
        mockMvc.perform(post(POST_CREATE_EMPLOYEE_URL)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)));
    }

    /**
     * Тест сервиса GET /auth/v1/changePassword (смена пароля)
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postChangePasswordSuccess() throws Exception {
        operationLogServiceStatistics.reset();

        ChangePasswordRq rq = new ChangePasswordRq();
        rq.setLogin("user");
        rq.setOldPassword("1");
        rq.setNewPassword("123Abc");

        mockMvc.perform(post(POST_CHANGE_PASSWORD_URL)
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("PASSWORD_CHANGE", logs.get(0).getOperationKey());
        assertEquals("user", logs.get(0).getOperationParameters().get("login"));
        assertEquals(OperationState.SUCCESS, logs.get(0).getOperationState());
    }

    /**
     * Тест сервиса GET /auth/v1/changePassword (смена пароля)
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postChangePasswordBadRequestWeakPassword() throws Exception {
        operationLogServiceStatistics.reset();

        ChangePasswordRq rq = new ChangePasswordRq();
        rq.setLogin("user");
        rq.setOldPassword("1");
        rq.setNewPassword("2");

        mockMvc.perform(post(POST_CHANGE_PASSWORD_URL)
                .content(TestUtils.convertObjectToJson(rq))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].errorMessage",
                        containsString("Пароль должен иметь длину не менее 6 символов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[1].errorMessage",
                        containsString("Пароль должен содержать не менее 3 различных символов")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[2].errorMessage",
                        containsString("Пароль обязательно должен содержать хотя бы по одному символу из "
                                + "следующих наборов: Цифры, Латинские прописные буквы, Латинские заглавные буквы")));

        List<OperationLogEntry> logs = operationLogServiceStatistics.getLogs();
        assertEquals(1, logs.size());
        assertEquals("PASSWORD_CHANGE", logs.get(0).getOperationKey());
        assertEquals("user", logs.get(0).getOperationParameters().get("login"));
        assertEquals(OperationState.CLIENT_ERROR, logs.get(0).getOperationState());
    }

    /**
     * Тест сервиса GET /auth/v1/resetPassword
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeForbiddenResetPassword() throws Exception {
        operationLogServiceStatistics.reset();

        mockMvc.perform(get(GET_RESET_PASSWORD_URL, "1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса GET /auth/v1/resetPassword
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeOkResetPassword() throws Exception {
        operationLogServiceStatistics.reset();

        mockMvc.perform(get(GET_RESET_PASSWORD_URL, "1")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * Тест сервиса GET /auth/v1/resetPassword
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldBeNotFoundResetPassword() throws Exception {
        operationLogServiceStatistics.reset();

        mockMvc.perform(get(GET_RESET_PASSWORD_URL, "4321341234")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    /**
     * Тест сервиса GET /auth/v1/private/client/resetPassword/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void resetClientPasswordSuccess() throws Exception {
        operationLogServiceStatistics.reset();

        mockMvc.perform(get(String.format(GET_RESET_CLIENT_PASSWORD_URL, "1", "123"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * Тест сервиса GET /auth/v1/private/client/resetPassword/{id}
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void resetClientPasswordNotFound() throws Exception {
        operationLogServiceStatistics.reset();

        mockMvc.perform(get(String.format(GET_RESET_CLIENT_PASSWORD_URL, "134565", "123"))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса POST /auth/v1/changeLogin
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postChangeLoginSuccess() throws Exception {
        operationLogServiceStatistics.reset();

        mockMvc.perform(get(String.format(GET_EMPLOYEE_URL, "9"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", is("client")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("Клиент")));

        ChangeLoginData loginData = new ChangeLoginData();
        loginData.setLogin("newTestLogin");
        mockMvc.perform(post(POST_USER_CHANGE_LOGIN)
                .content(TestUtils.convertObjectToJson(loginData))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_EMPLOYEE_URL, "9"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(9)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", is("newTestLogin")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", is("Клиент")));
    }

    /**
     * Тест сервиса POST /auth/v1/changeLogin
     * для проверки получения ошибки логина.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postChangeLoginError() throws Exception {
        operationLogServiceStatistics.reset();

        ChangeLoginData loginData = new ChangeLoginData();
        loginData.setLogin("vsp-head");
        mockMvc.perform(post(POST_USER_CHANGE_LOGIN)
                .content(TestUtils.convertObjectToJson(loginData))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CLIENT_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].fieldName", is("login")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].errorMessage", is("Пользователь с заданным логином уже существует")))
        ;
    }


    /**
     * Тест сервиса GET /auth/v1/changeLogin
     * для проверки получения ошибки.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postChangeLoginForbiden() throws Exception {
        operationLogServiceStatistics.reset();

        ChangeLoginData loginData = new ChangeLoginData();
        loginData.setLogin("newTestLogin");
        mockMvc.perform(post(POST_USER_CHANGE_LOGIN)
                .content(TestUtils.convertObjectToJson(loginData))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

}
