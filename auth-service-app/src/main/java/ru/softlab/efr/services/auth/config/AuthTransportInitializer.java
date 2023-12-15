package ru.softlab.efr.services.auth.config;

import org.springframework.web.WebApplicationInitializer;
import ru.softlab.efr.infrastructure.transport.server.init.AbstractTransportInitializer;

/**
 * Инициалайзер для программной настройки ServletContext в соответствии с спецификацией Servlet 3.0+
 *
 * @author niculichev
 * @since 24.04.2017
 */
public class AuthTransportInitializer extends AbstractTransportInitializer implements WebApplicationInitializer {

    @Override
    protected Class<?> getConfiguration() {
        return ApplicationConfig.class;
    }
}
