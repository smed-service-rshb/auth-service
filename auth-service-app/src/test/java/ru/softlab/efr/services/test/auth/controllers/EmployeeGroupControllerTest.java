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
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.auth.exchange.model.GroupData;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.test.auth.TestUtils;
import ru.softlab.efr.services.test.auth.config.TestRestApplicationConfig;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.test.auth.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;
import static ru.softlab.efr.services.test.auth.TestData.USER_PRINCIPAL_DATA;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestRestApplicationConfig.class)
public class EmployeeGroupControllerTest {

    private static final String GET_PAGE_EMPLOYEE_GROUP_URL = "/auth/v1/groups";
    private static final String GET_ALL_EMPLOYEE_GROUP_URL = "/auth/v1/private/groups";
    private static final String GET_EMPLOYEE_GROUP_BY_ID_URL = "/auth/v1/groups/{id}";
    private static final String POST_CREATE_EMPLOYEE_GROUP_URL = "/auth/v1/groups";
    private static final String PUT_UPDATE_EMPLOYEE_GROUPS_URL = "/auth/v1/groups/{id}";


    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * Тест сервиса "/auth/v1/groups" создание новой записи справочника
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createEmployeeGroupSuccess() throws Exception {
        GroupData data = new GroupData();
        data.setName("testData");
        data.setCode("testCode");
        mockMvc.perform(post(POST_CREATE_EMPLOYEE_GROUP_URL)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());
    }


    /**
     * Тест сервиса "/auth/v1/groups" создание записи справочника с незаполненным полем "код"
     * для проверки получения ошибки.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createEmployeeGroupError() throws Exception {
        GroupData data = new GroupData();
        data.setName("testData");
        mockMvc.perform(post(POST_CREATE_EMPLOYEE_GROUP_URL)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса "/auth/v1/groups" попытка сохранить запись с кодом, который уже существует в базе
     * для проверки получения ошибки.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createEmployeeGroupWithExistCodeError() throws Exception {
        GroupData data = new GroupData();
        data.setName("testData");
        data.setCode("CODE_1");
        mockMvc.perform(post(POST_CREATE_EMPLOYEE_GROUP_URL)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса "/auth/v1/groups" попытка сохранить запись при отсутствии прав
     * для проверки получения ошибки.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createEmployeeGroupWithOutRightError() throws Exception {
        GroupData data = new GroupData();
        data.setName("testData");
        data.setCode("testCode");
        mockMvc.perform(post(POST_CREATE_EMPLOYEE_GROUP_URL)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса "/auth/v1/groups/{id}" обновление записи справочника
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateEmployeeGroupSuccess() throws Exception {
        GroupData data = new GroupData();
        data.setName("testData");
        data.setCode("testCode");
        mockMvc.perform(put(PUT_UPDATE_EMPLOYEE_GROUPS_URL, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }


    /**
     * Тест сервиса "/auth/v1/groups/{id}" обновление записи справочника с незаполненным полем "код"
     * для проверки получения ошибки.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateEmployeeGroupError() throws Exception {
        GroupData data = new GroupData();
        data.setName("testData");
        mockMvc.perform(put(PUT_UPDATE_EMPLOYEE_GROUPS_URL, 1)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса "/auth/v1/groups/{id}" обновление попытка сохранить запись с кодом, который уже существует в базе
     * для проверки получения ошибки.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateEmployeeGroupWithExistCodeError() throws Exception {
        GroupData data = new GroupData();
        data.setName("testData");
        data.setCode("CODE_1");
        mockMvc.perform(put(PUT_UPDATE_EMPLOYEE_GROUPS_URL, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест сервиса "/auth/v1/groups/{id}" обновление попытка сохранить запись при отсутствии прав
     * для проверки получения ошибки.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateEmployeeGroupWithOutRightError() throws Exception {
        GroupData data = new GroupData();
        data.setName("testData");
        data.setCode("testCode");
        mockMvc.perform(put(PUT_UPDATE_EMPLOYEE_GROUPS_URL, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(data))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса "/auth/v1/groups" получение постраничного просмотра
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getEmployeeGroupPageListSuccess() throws Exception {
        mockMvc.perform(get(GET_PAGE_EMPLOYEE_GROUP_URL).param("sort", "id")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].code", is("CODE_1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", is("NAME_1")));
    }

    /**
     * Тест сервиса "/auth/v1/groups" получение постраничного просмотра без наличия прав
     * для проверки получения ошибки.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getEmployeeGroupPageListError() throws Exception {
        mockMvc.perform(get(GET_PAGE_EMPLOYEE_GROUP_URL).param("sort", "id")
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    /**
     * Тест сервиса "/auth/v1/private/groups" получение списка
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getEmployeeGroupListSuccess() throws Exception {
        mockMvc.perform(get(GET_ALL_EMPLOYEE_GROUP_URL)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groups").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.groups", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groups[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groups[0].code", is("CODE_1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groups[0].name", is("NAME_1")));
    }

    /**
     * Тест сервиса "/auth/v1/groups/{id}" получение записи по идентификатору
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getEmployeeGroupByIdSuccess() throws Exception {
        mockMvc.perform(get(GET_EMPLOYEE_GROUP_BY_ID_URL, 2)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", is("CODE_2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("NAME_2")));
    }

    /**
     * Тест сервиса "/auth/v1/groups/{id}" получение записи по идентификатору при отсутствии записи
     * для проверки получения ошибки.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getEmployeeGroupByIdNotFound() throws Exception {
        mockMvc.perform(get(GET_EMPLOYEE_GROUP_BY_ID_URL, 10)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    /**
     * Тест сервиса "/auth/v1/groups/{id}" получение записи по идентификатору при отсутствии прав
     * для проверки получения ошибки.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getEmployeeGroupByIdForbidden() throws Exception {
        mockMvc.perform(get(GET_EMPLOYEE_GROUP_BY_ID_URL, 10)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }
}
