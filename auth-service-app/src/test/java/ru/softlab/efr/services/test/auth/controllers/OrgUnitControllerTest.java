package ru.softlab.efr.services.test.auth.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.controllers.OrgUnitController;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.test.auth.config.TestRestApplicationConfig;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestRestApplicationConfig.class)
public class OrgUnitControllerTest {

    private static final String GET_ORGUNITS_REQUEST = "/auth/v1/orgunits";
    private static final String GET_ORGUNITS_TYPE_REQUEST = "/auth/v1/orgunits/type/%s";
    private static final String GET_ORGUNITS_CHILDREN_REQUEST = "/auth/v1/orgunits/children/%s";

    private static final PrincipalDataImpl ADMIN_PRINCIPAL_DATA;

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

    @InjectMocks
    private OrgUnitController orgUnitController;

    @Before
    public void setUp() { this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build(); }

    /**
     * Тест сервиса GET /auth/v1/orgunits получения полного списка организационных структур
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOrgUnitsSuccess() throws Exception {

        mockMvc.perform(get(GET_ORGUNITS_REQUEST)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(ADMIN_PRINCIPAL_DATA)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orgUnits", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orgUnits[0].branchId", is(1000001)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orgUnits[0].name", is("ДРБ")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orgUnits[0].offices", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orgUnits[0].offices[0].officeId", is(2000001)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orgUnits[0].offices[0].name", is("0001")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orgUnits[0].offices[0].city", is("Москва")));
    }
    /**
     * Тест сервиса GET /auth/v1/orgunits/children/{id} получения списка дочерних организационных структур
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOrgUnitChildrenSuccess() throws Exception {

        mockMvc.perform(get(String.format(GET_ORGUNITS_CHILDREN_REQUEST, "1000001"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(ADMIN_PRINCIPAL_DATA)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orgUnits", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orgUnits[0].branch", is("ДРБ")));
    }
    /**
     * Тест сервиса GET /auth/v1/orgunits/children/{id} получения списка дочерних организационных структур
     * для проверки получения корректного ответа.
     *
     * @throws Exception при падении теста
     */
    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getOrgUnitByTypeSuccess() throws Exception {

        mockMvc.perform(get(String.format(GET_ORGUNITS_TYPE_REQUEST, "0"))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(ADMIN_PRINCIPAL_DATA)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orgUnits", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orgUnits[0].office", is("ДРБ")));
    }
}
