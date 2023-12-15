package ru.softlab.efr.services.test.testapp.someservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.softlab.efr.services.testapps.someservice.config.ApplicationConfig;
import ru.softlab.efr.test.infrastructure.transport.rest.config.AbstractTestConfiguration;
import ru.softlab.efr.test.services.auth.rest.config.AuthTestConfiguration;

@Import({AuthTestConfiguration.class})
@Configuration
public class TestClientConfiguration extends AbstractTestConfiguration {
    @Override
    protected String getTestAppName() {
        return ApplicationConfig.SERVICE_NAME;
    }

    @Override
    protected String getTestAppContextRoot() {
        return "/some-service";
    }
}
