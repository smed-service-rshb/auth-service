package ru.softlab.efr.services.test.testapp.someservice;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.softlab.efr.services.test.testapp.someservice.config.TestClientConfiguration;
import ru.softlab.efr.test.infrastructure.transport.rest.MockRestRule;

import static org.hamcrest.CoreMatchers.is;
import static ru.softlab.efr.test.infrastructure.transport.rest.matchers.MockRestResultMatchers.content;
import static ru.softlab.efr.test.infrastructure.transport.rest.matchers.MockRestResultMatchers.status;

@SuppressWarnings("Duplicates")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestClientConfiguration.class)
public class CommonIntegrationTest {
    @Autowired
    @Rule
    public MockRestRule mockRest;

    @Test
    public void should_Success_When_PreFlightRequest() throws Exception {
        mockRest.init()
                .path("/test-message")
                .header(HttpHeaders.ORIGIN, "some_origin")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                .options()
                .andExpect(status().isOk());
    }

    @Test
    public void testExceptions() throws Exception {
        String message = "sdfsdfsdf";
        mockRest.init()
                .path("/exception/{message}")
                .variable("message", message)
                .get(String.class)
                .andExpect(status().is(HttpStatus.NOT_ACCEPTABLE))
                .andExpect(content().body(is(message)))
        ;
    }
}
