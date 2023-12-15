package ru.softlab.efr.services.testapps.someservice.config;

import org.springframework.web.WebApplicationInitializer;
import ru.softlab.efr.infrastructure.transport.server.init.AbstractTransportInitializer;

/**
 * @author niculichev
 * @since 23.05.2017
 */
public class ApplicationInitializer extends AbstractTransportInitializer implements WebApplicationInitializer {

    @Override
    protected Class<?> getConfiguration() {
        return ApplicationConfig.class;
    }
}
