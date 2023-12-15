package ru.softlab.efr.services.test.auth.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;
import ru.softlab.efr.services.auth.exchange.model.*;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.test.auth.TestUtils;
import ru.softlab.efr.services.test.auth.config.TestRestApplicationConfig;
import ru.softlab.efr.services.test.auth.controllers.helpers.TestMotivationReportsHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.softlab.efr.services.test.auth.TestData.CHIEF_ADMIN_PRINCIPAL_DATA;
import static ru.softlab.efr.services.test.auth.TestData.USER_PRINCIPAL_DATA;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestRestApplicationConfig.class)
public class MotivationControllerTest {

    private static final String GET_MOTIVATION = "/auth/v1/motivation";
    private static final String POST_MOTIVATION = "/auth/v1/motivation";
    private static final String POST_MOTIVATION_DOCUMENT = "/auth/v1/motivation/%s/attach-document/";
    private static final String GET_MOTIVATION_DOCUMENT = "/auth/v1/motivation/%s/attach-document/";
    private static final String GET_PRINT_MOTIVATION = "/auth/v1/motivation/print";
    private static final String POST_MOTIVATION_EDIT = "/auth/v1/motivation/%s/edit";
    private static final String GET_MOTIVATION_DOCUMENT_BY_ID = "/auth/v1/motivation/%s/get-document/%s";
    private static final String GET_LATEST_MOTIVATION_BY_USER_ID = "/auth/v1/motivation/%s/get-latest";
    private static final String GET_MOTIVATIONS_LIST_BY_USER_ID = "/auth/v1/motivation/%s/get-hystory-list";
    private static final String GET_MOTIVATION_HIDE_WINDOW = "/auth/v1/motivation/hide-window";
    private static final String GET_MOTIVATION_REPORT = "/auth/v1/motivation/report?startDate={startDate}&endDate={endDate}";

    private static final Long TEST_USER_ID = USER_PRINCIPAL_DATA.getId();

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();
    private static final Resource INPUT_STREAM = new ClassPathResource("test.png");

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    public void createReportTestData() throws Exception {
        MotivationProgramUserModify motivationProgramUserModify = new MotivationProgramUserModify();
        motivationProgramUserModify.setAccountNumber("12345678901234567890");
        motivationProgramUserModify.setBankName("test");
        motivationProgramUserModify.setBikCode("123456789");
        motivationProgramUserModify.setInn("123456789012");
        motivationProgramUserModify.setStartDate(LocalDate.now().minusMonths(3));

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().minusMonths(3).toString())));

        motivationProgramUserModify.setStartDate(null);

        ResultActions motivationResponse = mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_MOTIVATION_DOCUMENT, MotivationDocumentTypes.CHECK_FORM.toString()))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_MOTIVATION_DOCUMENT, MotivationDocumentTypes.PASSPORT.toString()))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_MOTIVATION_HIDE_WINDOW)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(true)));

        MotivationSettingsEditAdmin editSettings = new MotivationSettingsEditAdmin();
        editSettings.setAccountNumber("12345678901234567999");
        editSettings.setMotivationCorrectStatus(MotivationCorrectStatus.CORRECT);

        mockMvc.perform(post(String.format(
                POST_MOTIVATION_EDIT,
                TestUtils.extractDataFromResultJson(motivationResponse, "$.id").toString())
        )
                .content(TestUtils.convertObjectToJson(editSettings))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

    }

    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createMotivationReportTest() throws Exception {
        createReportTestData();

        ResultActions motivationResponse = mockMvc.perform(get(GET_MOTIVATION_REPORT, LocalDate.now().withDayOfMonth(1), LocalDate.now())
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")));
        ByteArrayInputStream inputFile = new ByteArrayInputStream(motivationResponse.andReturn().getResponse().getContentAsByteArray());
        Assert.assertEquals(3, TestMotivationReportsHelper.readExcelSheetRowsCount(inputFile));

        /*      раскоментировать для сохранения файла локально
        OutputStream os = new FileOutputStream(new File("./test.xlsx"));
        os.write(motivationResponse.andReturn().getResponse().getContentAsByteArray());
        os.close();
        */
    }


    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setPostAndGetMotivationExpiredTest() throws Exception {
        MotivationProgramUserModify motivationProgramUserModify = new MotivationProgramUserModify();
        motivationProgramUserModify.setAccountNumber("12345678901234567890");
        motivationProgramUserModify.setBankName("test");
        motivationProgramUserModify.setBikCode("123456789");
        motivationProgramUserModify.setInn("123456789012");
        motivationProgramUserModify.setStartDate(LocalDate.now().minusMonths(3));

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().minusMonths(3).toString())));

        mockMvc.perform(get(GET_MOTIVATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").isEmpty());
    }

    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setPostAndGetMotivationExpiredAndCreationAgainTest() throws Exception {
        MotivationProgramUserModify motivationProgramUserModify = new MotivationProgramUserModify();
        motivationProgramUserModify.setAccountNumber("12345678901234567890");
        motivationProgramUserModify.setBankName("test");
        motivationProgramUserModify.setBikCode("123456789");
        motivationProgramUserModify.setInn("123456789012");
        motivationProgramUserModify.setStartDate(LocalDate.now().minusMonths(3));

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().minusMonths(3).toString())));

        mockMvc.perform(get(GET_MOTIVATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").isEmpty());

        motivationProgramUserModify.setStartDate(null);

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));

        mockMvc.perform(get(GET_MOTIVATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));
    }

    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setPostAndGetMotivationTest() throws Exception {
        MotivationProgramUserModify motivationProgramUserModify = new MotivationProgramUserModify();
        motivationProgramUserModify.setAccountNumber("12345678901234567890");
        motivationProgramUserModify.setBankName("test");
        motivationProgramUserModify.setBikCode("123456789");
        motivationProgramUserModify.setInn("123456789012");

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));

        mockMvc.perform(get(GET_MOTIVATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));
    }


    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setPostAndGetMotivationOnDateTest() throws Exception {
        MotivationProgramUserModify motivationProgramUserModify = new MotivationProgramUserModify();
        motivationProgramUserModify.setAccountNumber("12345678901234567890");
        motivationProgramUserModify.setBankName("test");
        motivationProgramUserModify.setBikCode("123456789");
        motivationProgramUserModify.setInn("123456789012");
        motivationProgramUserModify.setStartDate(LocalDate.now());

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));

        motivationProgramUserModify.setStartDate(LocalDate.now().plusDays(10));

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].key", is("Проверка допустимости выбранной даты начала мотивации")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].value", is("Недопустимо указывать дату начала мотивации больше текущей даты")));

        motivationProgramUserModify.setStartDate(LocalDate.now().minusYears(10));

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].key", is("Проверка допустимости выбранной даты начала мотивации")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].value", is("Недопустимо указывать дату начала мотивации меньше даты первой авторизации в систему")));
    }

    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void setPostAndGetMotivationOnSameDateTest() throws Exception {
        MotivationProgramUserModify motivationProgramUserModify = new MotivationProgramUserModify();
        motivationProgramUserModify.setAccountNumber("12345678901234567890");
        motivationProgramUserModify.setBankName("test");
        motivationProgramUserModify.setBikCode("123456789");
        motivationProgramUserModify.setInn("123456789012");
        motivationProgramUserModify.setStartDate(LocalDate.of(2019, 1, 1));

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.of(2019, 1, 1).toString())));
    }

    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void motivationHideWindowTest() throws Exception {
        mockMvc.perform(get(GET_MOTIVATION_HIDE_WINDOW)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(false)));

        MotivationProgramUserModify motivationProgramUserModify = new MotivationProgramUserModify();
        motivationProgramUserModify.setAccountNumber("12345678901234567890");
        motivationProgramUserModify.setBankName("test");
        motivationProgramUserModify.setBikCode("123456789");
        motivationProgramUserModify.setInn("123456789012");

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));

        ResultActions motivationResponse = mockMvc.perform(get(GET_MOTIVATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));

        mockMvc.perform(get(GET_MOTIVATION_HIDE_WINDOW)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(false)));

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_MOTIVATION_DOCUMENT, MotivationDocumentTypes.CHECK_FORM.toString()))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_MOTIVATION_DOCUMENT, MotivationDocumentTypes.PASSPORT.toString()))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_MOTIVATION_HIDE_WINDOW)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(true)));

        MotivationSettingsEditAdmin editSettings = new MotivationSettingsEditAdmin();
        editSettings.setAccountNumber("12345678901234567999");
        editSettings.setMotivationCorrectStatus(MotivationCorrectStatus.CORRECT);

        mockMvc.perform(post(String.format(
                POST_MOTIVATION_EDIT,
                TestUtils.extractDataFromResultJson(motivationResponse, "$.id").toString())
        )
                .content(TestUtils.convertObjectToJson(editSettings))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_MOTIVATION_HIDE_WINDOW)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$", is(true)));
    }

    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void attachAndGetDocumentToMotivationTest() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        MotivationProgramUserModify motivationProgramUserModify = new MotivationProgramUserModify();
        motivationProgramUserModify.setAccountNumber("12345678901234567890");
        motivationProgramUserModify.setBankName("test");
        motivationProgramUserModify.setBikCode("123456789");
        motivationProgramUserModify.setInn("123456789012");

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));

        mockMvc.perform(get(GET_MOTIVATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_MOTIVATION_DOCUMENT, MotivationDocumentTypes.CHECK_FORM))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(GET_MOTIVATION_DOCUMENT, MotivationDocumentTypes.CHECK_FORM))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("application/octet-stream")))
                .andExpect(content().bytes(firstFile.getBytes()));
    }

    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getMotivationPrintFormTest() throws Exception {
        mockMvc.perform(get(GET_PRINT_MOTIVATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document")));
    }

    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void editMotivationTest() throws Exception {
        MotivationProgramUserModify motivationProgramUserModify = new MotivationProgramUserModify();
        motivationProgramUserModify.setAccountNumber("12345678901234567890");
        motivationProgramUserModify.setBankName("test");
        motivationProgramUserModify.setBikCode("123456789");
        motivationProgramUserModify.setInn("123456789012");

        ResultActions motivationResponse = mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));
        ;

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));
        ;

        MotivationSettingsEditAdmin editSettings = new MotivationSettingsEditAdmin();
        editSettings.setAccountNumber("12345678901234567999");
        editSettings.setComment("test");

        mockMvc.perform(post(String.format(
                POST_MOTIVATION_EDIT,
                TestUtils.extractDataFromResultJson(motivationResponse, "$.id").toString())
        )
                .content(TestUtils.convertObjectToJson(editSettings))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_MOTIVATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));

        editSettings.setMotivationCorrectStatus(MotivationCorrectStatus.CORRECT);

        mockMvc.perform(post(String.format(
                POST_MOTIVATION_EDIT,
                TestUtils.extractDataFromResultJson(motivationResponse, "$.id").toString())
        )
                .content(TestUtils.convertObjectToJson(editSettings))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(HttpStatus.NOT_ACCEPTABLE.value()));

        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "text/plain",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_MOTIVATION_DOCUMENT, MotivationDocumentTypes.CHECK_FORM))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_MOTIVATION_DOCUMENT, MotivationDocumentTypes.PASSPORT))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        mockMvc.perform(post(String.format(
                POST_MOTIVATION_EDIT,
                TestUtils.extractDataFromResultJson(motivationResponse, "$.id").toString())
        )
                .content(TestUtils.convertObjectToJson(editSettings))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(GET_MOTIVATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567999")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("CORRECT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName", is("test.png")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName", is("test.png")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));
    }

    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDocumentByIdTest() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile(
                "content", INPUT_STREAM.getFilename(), "image/png",
                StreamUtils.copyToByteArray(INPUT_STREAM.getInputStream()));

        MotivationProgramUserModify motivationProgramUserModify = new MotivationProgramUserModify();
        motivationProgramUserModify.setAccountNumber("12345678901234567890");
        motivationProgramUserModify.setBankName("test");
        motivationProgramUserModify.setBikCode("123456789");
        motivationProgramUserModify.setInn("123456789012");

        ResultActions motivationResponse = mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));
        ;

        mockMvc.perform(get(GET_MOTIVATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));


        mockMvc.perform(MockMvcRequestBuilders.fileUpload(String.format(POST_MOTIVATION_DOCUMENT, MotivationDocumentTypes.CHECK_FORM))
                .file(firstFile)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA)))
                .andExpect(status().isOk());

        mockMvc.perform(get(String.format(
                GET_MOTIVATION_DOCUMENT_BY_ID,
                TestUtils.extractDataFromResultJson(motivationResponse, "$.id").toString(),
                MotivationDocumentTypes.CHECK_FORM)
        )
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType("application/octet-stream")))
                .andExpect(content().bytes(firstFile.getBytes()))
                .andExpect(header().string("Content-Disposition", "attachment; filename=test.png"));
    }

    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getMotivationByUserIdTest() throws Exception {
        MotivationProgramUserModify motivationProgramUserModify = new MotivationProgramUserModify();
        motivationProgramUserModify.setAccountNumber("12345678901234567890");
        motivationProgramUserModify.setBankName("test");
        motivationProgramUserModify.setBikCode("123456789");
        motivationProgramUserModify.setInn("123456789012");

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));
        ;

        mockMvc.perform(get(GET_MOTIVATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));

        mockMvc.perform(get(String.format(
                GET_LATEST_MOTIVATION_BY_USER_ID,
                TEST_USER_ID)
        )
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));
    }

    @Test
    @Sql(value = {
            "classpath:create_test_data.sql",
            "classpath:test_data_set_user_login_attempt_settings.sql",
            "classpath:create_test_data_session.sql"},
            config = @SqlConfig(encoding = "UTF-8"))
    @Sql(value = "classpath:delete_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getMotivationListByUserIdTest() throws Exception {
        MotivationProgramUserModify motivationProgramUserModify = new MotivationProgramUserModify();
        motivationProgramUserModify.setAccountNumber("12345678901234567890");
        motivationProgramUserModify.setBankName("test");
        motivationProgramUserModify.setBikCode("123456789");
        motivationProgramUserModify.setInn("123456789012");

        mockMvc.perform(post(POST_MOTIVATION)
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));
        ;

        mockMvc.perform(get(GET_MOTIVATION)
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(USER_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber", is("12345678901234567890")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate", is(LocalDate.now().toString())));

        mockMvc.perform(get(String.format(
                GET_MOTIVATIONS_LIST_BY_USER_ID,
                TEST_USER_ID)
        )
                .content(TestUtils.convertObjectToJson(motivationProgramUserModify))
                .header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(CHIEF_ADMIN_PRINCIPAL_DATA))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements", hasSize(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].accountNumber").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].bikCode", is("123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].bankName", is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].inn", is("123456789012")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].registrationAddress").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].index").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].comment").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].motivationCorrectStatus", is("NOT_CHECKED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].documentName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].passportName").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elements[0].startDate", is(LocalDate.now().toString())));
    }

}
