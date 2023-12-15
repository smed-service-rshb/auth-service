package ru.softlab.efr.services.test.auth.integration;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.ListenableFuture;
import ru.softlab.efr.infrastructure.transport.client.impl.JMSMicroServiceTemplate;
import ru.softlab.efr.infrastructure.transport.client.impl.JmsUriBuilder;
import ru.softlab.efr.services.auth.AuthServiceInfo;
import ru.softlab.efr.services.test.auth.config.TestApplicationConfig;

import java.net.URI;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ru.softlab.efr.services.auth.AuthServiceInfo.SERVICE_NAME;
import static ru.softlab.efr.services.test.auth.integration.EmployeeIntegrationTestData.CORRECT_EMPLOYEE;
import static ru.softlab.efr.services.test.auth.integration.EmployeeIntegrationTestData.USERS_CORRECT_PASSWORD;

/**
 * @author niculichev
 * @ created 21.04.2017
 * @ $Author$
 * @ $Revision$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@Ignore
public class AuthSessionAsyncIntegrationTest extends IntegrationTestBase {
    @Autowired
    private JMSMicroServiceTemplate microServiceTemplate;

    // *************************** ТЕСТЫ *****************************************

    @Test
    public void should_Success_When_CorrectCredentials() throws Exception {
        JSONObject body = new JSONObject();
        body.put("login", CORRECT_EMPLOYEE.getLogin());
        body.put("password", USERS_CORRECT_PASSWORD);


        HttpResponse response = Executor.newInstance(createClient())
                .execute(Request
                        .Post(getServiceUrl() + "/session")
                        .bodyString(body.toString(), ContentType.APPLICATION_JSON))
                .returnResponse();

        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.OK.value()));
    }

    @Test
    public void should_Success_When_CorrectCredentials_async() throws Exception {
        JSONObject body = new JSONObject();
        body.put("login", CORRECT_EMPLOYEE.getLogin());
        body.put("password", USERS_CORRECT_PASSWORD);

        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(AuthServiceInfo.SERVICE_PREFIX + "/session")
                .build();

        RequestEntity<String> requestEntity = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(body.toString());

        ListenableFuture<ResponseEntity<String>> future = microServiceTemplate.exchange(requestEntity, String.class);
        assertThat(getStatusCode(future, 10), is(HttpStatus.OK)); // взаимодействие с active directory может быть не быстрым.
    }
}