package ru.softlab.efr.services.testapps.someservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.softlab.efr.common.utilities.hibernate.annotations.EnableHibernateJpa;
import ru.softlab.efr.infrastructure.transport.annotations.EnableTransport;
import ru.softlab.efr.services.authorization.annotations.EnablePermissionControl;

/**
 * Основная конфигурация для всего приложения
 *
 * @author niculichev
 * @since 23.05.2017
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ru.softlab.efr.services.testapps.someservice")
@EnableHibernateJpa
@EnablePermissionControl
@EnableTransport(ApplicationConfig.SERVICE_NAME)
public class ApplicationConfig {
    public static final String SERVICE_NAME = "some-service";
}

