package ru.softlab.efr.services.test.auth.controllers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import ru.softlab.efr.services.auth.exchange.model.ShortCodeRequest;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.services.UserLoginAttemptsService;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.SessionRepository;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserRepository;
import ru.softlab.efr.services.test.auth.TestUtils;
import ru.softlab.efr.services.test.auth.config.TestRestApplicationConfig;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestRestApplicationConfig.class)
public class MobileAuthControllerTest {

    private static final String POST_MOBILE_LOGIN_URL = "/auth/v1/mobile-login";
    private static final String POST_SHORT_CODE_URL = "/auth/v1/short-code";
    private static final Long TEST_USER_ID = 5L;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setSessionRepository(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Test
    @Sql(value = {"classpath:create_test_data.sql", "classpath:test_data_set_user_login_attempt_settings.sql"}, config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void failedAuthenticationByWrongPassword() throws Exception {
        Employee user = userRepository.findOne(TEST_USER_ID);
        MobileLoginRequest body = new MobileLoginRequest();
        body.setAuthMethod(MobileLoginRequest.AuthMethodEnum.NAME_AND_PASSWORD);
        body.setLogin(user.getLogin());
        body.setPassword("wrong passwd");
        mockMvc.perform(post(POST_MOBILE_LOGIN_URL)
                .content(TestUtils.convertObjectToJson(body))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void loginCorrect() throws Exception {
        Employee user = userRepository.findOne(TEST_USER_ID);
        MobileLoginRequest body = new MobileLoginRequest();
        body.setAuthMethod(MobileLoginRequest.AuthMethodEnum.NAME_AND_PASSWORD);
        body.setLogin(user.getLogin());
        body.setPassword("12345Abc"); //hash 58e9d0e4808c7b43fdc17aa52cd1ed46d7012fc8
        mockMvc.perform(post(POST_MOBILE_LOGIN_URL)
                .content(TestUtils.convertObjectToJson(body))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", is(user.getEmail())));
    }

    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void loginAndRegisterDevice() throws Exception {
        Employee user = userRepository.findOne(TEST_USER_ID);
        MobileLoginRequest mobileLoginRequest = new MobileLoginRequest();
        mobileLoginRequest.setAuthMethod(MobileLoginRequest.AuthMethodEnum.NAME_AND_PASSWORD);
        mobileLoginRequest.setLogin(user.getLogin());
        mobileLoginRequest.setPassword("12345Abc"); //hash 58e9d0e4808c7b43fdc17aa52cd1ed46d7012fc8
        ResultActions mobileLoginRequestResultActions = mockMvc.perform(post(POST_MOBILE_LOGIN_URL)
                .content(TestUtils.convertObjectToJson(mobileLoginRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", is(user.getEmail())));
        String id = mobileLoginRequestResultActions.andReturn().getResponse().getContentAsString().substring(7, 43);
        ShortCodeRequest shortCodeRequest = new ShortCodeRequest();
        shortCodeRequest.setSessionId(id);
        shortCodeRequest.setShortCode("0666");
        shortCodeRequest.setPlatform(ShortCodeRequest.PlatformEnum.IOS);
        shortCodeRequest.setIdentifierDevice("12q121-1213k-1323-123m");
        mockMvc.perform(post(POST_SHORT_CODE_URL)
                .content(TestUtils.convertObjectToJson(shortCodeRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty());
    }

    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void loginWithUnregisteredDevice() throws Exception {
        MobileLoginRequest mobileLoginRequestByDevice = new MobileLoginRequest();
        mobileLoginRequestByDevice.setAuthMethod(MobileLoginRequest.AuthMethodEnum.SHORT_CODE);
        mobileLoginRequestByDevice.setLogin("none");
        mobileLoginRequestByDevice.setPassword("0666");
        mockMvc.perform(post(POST_SHORT_CODE_URL)
                .content(TestUtils.convertObjectToJson(mobileLoginRequestByDevice))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(value = "classpath:create_test_data.sql", config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void loginByRegisteredDevice() throws Exception {
        Employee user = userRepository.findOne(TEST_USER_ID);
        MobileLoginRequest mobileLoginRequest = new MobileLoginRequest();
        mobileLoginRequest.setAuthMethod(MobileLoginRequest.AuthMethodEnum.NAME_AND_PASSWORD);
        mobileLoginRequest.setLogin(user.getLogin());
        mobileLoginRequest.setPassword("12345Abc"); //hash 58e9d0e4808c7b43fdc17aa52cd1ed46d7012fc8
        ResultActions mobileLoginRequestResultActions = mockMvc.perform(post(POST_MOBILE_LOGIN_URL)
                .content(TestUtils.convertObjectToJson(mobileLoginRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", is(user.getEmail())));

        String id = mobileLoginRequestResultActions.andReturn().getResponse().getContentAsString().substring(7, 43);
        ShortCodeRequest shortCodeRequest = new ShortCodeRequest();
        shortCodeRequest.setSessionId(id);
        shortCodeRequest.setShortCode("0666");
        shortCodeRequest.setPlatform(ShortCodeRequest.PlatformEnum.IOS);
        shortCodeRequest.setIdentifierDevice("12q121-1213k-1323-123m");
        ResultActions shortCodeActions = mockMvc.perform(post(POST_SHORT_CODE_URL)
                .content(TestUtils.convertObjectToJson(shortCodeRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty());

        String jsonTokenResponse = shortCodeActions.andReturn().getResponse().getContentAsString();
        String tokenInBrackets = jsonTokenResponse.substring(jsonTokenResponse.indexOf(":") + 1, jsonTokenResponse.indexOf("}"));
        String token = tokenInBrackets.substring(1, tokenInBrackets.length() - 1);
        sessionRepository.close(id);

        MobileLoginRequest mobileLoginRequestByDevice = new MobileLoginRequest();
        mobileLoginRequestByDevice.setAuthMethod(MobileLoginRequest.AuthMethodEnum.SHORT_CODE);
        mobileLoginRequestByDevice.setLogin(token);
        mobileLoginRequestByDevice.setPassword("0666");
        mockMvc.perform(post(POST_MOBILE_LOGIN_URL)
                .content(TestUtils.convertObjectToJson(mobileLoginRequestByDevice))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.email", is(user.getEmail())));
    }

}
