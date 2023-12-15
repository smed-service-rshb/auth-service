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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.auth.exchange.model.MobileLoginRequest;
import ru.softlab.efr.services.auth.exchange.model.MotivationSettings;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.test.auth.TestUtils;
import ru.softlab.efr.services.test.auth.config.TestRestApplicationConfig;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.test.auth.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestRestApplicationConfig.class)
public class MotivationCheckSettingsControllerTest {

    private static final String GET_MOTIVATION_CONFIG = "/auth/v1/settings/motivation-config";
    private static final String POST_MOTIVATION_CONFIG = "/auth/v1/settings/motivation-config";

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_set_user_login_attempt_settings.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getMotivationConfigTest() throws Exception {

        mockMvc.perform(get(GET_MOTIVATION_CONFIG)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isEnabled", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expireTime", is(1)));
    }

    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_set_user_login_attempt_settings.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setMotivationConfigTest() throws Exception {
        MotivationSettings motivationSettings = new MotivationSettings();
        motivationSettings.setExpireTime(15);
        motivationSettings.setIsEnabled(true);

        mockMvc.perform(post(POST_MOTIVATION_CONFIG)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(motivationSettings))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_MOTIVATION_CONFIG)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isEnabled", is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expireTime", is(15)));
    }


}
