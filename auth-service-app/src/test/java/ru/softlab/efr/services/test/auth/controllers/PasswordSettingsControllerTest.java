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
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.test.auth.TestUtils;
import ru.softlab.efr.services.test.auth.config.TestRestApplicationConfig;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.test.auth.MockData.getPasswordCheckSettings;
import static ru.softlab.efr.services.test.auth.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;
import static ru.softlab.efr.services.test.auth.TestData.USER_PRINCIPAL_DATA;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestRestApplicationConfig.class)
public class PasswordSettingsControllerTest {

    private static final String PUT_PASSWORD_SETTINGS = "/auth/v1/settings/password";
    private static final String GET_PASSWORD_SETTINGS = "/auth/v1/settings/password";

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateAndGetPasswordCheckSettings() throws Exception {

        mockMvc.perform(put(PUT_PASSWORD_SETTINGS)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(getPasswordCheckSettings()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_PASSWORD_SETTINGS)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.checkEnabled", is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.minLength", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.maxLength", is(60)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfDifferentCharacters", is(8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.specialCharsets", is("!@#$%^&*()-_+=")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enabledCharsets[0]", is("LOWERCASE_LATIN")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.enabledCharsets[1]", is("SPECIAL")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requiredCharsets[0]", is("DIGIT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requiredCharsets[1]", is("UPPERCASE_LATIN")));
    }

    @Test
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updatePasswordCheckSettingsForbidden() throws Exception {
        mockMvc.perform(put(PUT_PASSWORD_SETTINGS)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .content(TestUtils.convertObjectToJson(getPasswordCheckSettings()))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getPasswordCheckSettingsForbidden() throws Exception {
        mockMvc.perform(get(GET_PASSWORD_SETTINGS)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isForbidden());
    }
}