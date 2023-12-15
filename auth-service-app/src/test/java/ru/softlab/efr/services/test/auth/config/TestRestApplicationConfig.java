package ru.softlab.efr.services.test.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.softlab.efr.common.client.PrintTemplatesClient;
import ru.softlab.efr.common.settings.annotations.EnableSettings;
import ru.softlab.efr.common.utilities.hibernate.annotations.EnableHibernateJpa;
import ru.softlab.efr.common.utilities.rest.converters.config.RestDataConverterConfig;
import ru.softlab.efr.infrastructure.logging.annotations.EnableLogging;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.services.OperationLogService;
import ru.softlab.efr.infrastructure.transport.annotations.EnableTransport;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;
import ru.softlab.efr.services.authorization.annotations.EnablePermissionControl;
import ru.softlab.efr.services.test.auth.OperationLogServiceStatistics;
import ru.softlab.efr.services.test.auth.stubs.PrintTemplatesClientStub;

import javax.sql.DataSource;

import java.util.Calendar;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

/**
 * Конфигурационный класс запуска тестов для rest сервисов с использованием H2 DB
 *
 * @author krivenko
 * @since 15.08.2018
 */
@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan(
        basePackages = {
                "ru.softlab.efr.services.auth",
                "ru.softlab.efr.common.client"
        },
        excludeFilters = @ComponentScan.Filter(
                value = Configuration.class,
                type = ANNOTATION
        ))
@EnablePermissionControl
@Import({RestDataConverterConfig.class})
@EnableHibernateJpa(
        repositoryPackages = {
                "ru.softlab.efr.services.auth.services.impl.dao.repositories",
                "ru.softlab.efr.services.authorization.repositories"},
        modelPackages = {
                "ru.softlab.efr.services.auth.model",
                "ru.softlab.efr.services.authorization.model"})
@EnableTransport(TestRestApplicationConfig.APPLICATION_NAME)
@EnableSettings
@EnableLogging
@PropertySource("classpath:test-settings.unit-test.properties")
public class TestRestApplicationConfig {
    static final String APPLICATION_NAME = "auth-service";

    @Bean
    @Primary
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder
                .setType(EmbeddedDatabaseType.H2)
                .setName(APPLICATION_NAME)
                .build();
    }

    @Bean
    public OperationLogServiceStatistics operationLogServiceStatistics() {
        return new OperationLogServiceStatistics();
    }

    @Bean
    @Primary
    public OperationLogService operationLogService(OperationLogServiceStatistics operationLogServiceStatistics) {
        return new OperationLogService() {
            @Override
            public OperationLogEntry startLogging() {
                OperationLogEntry operationLogEntry = new OperationLogEntry();
                operationLogEntry.setLogDate(Calendar.getInstance());
                return operationLogEntry;
            }

            @Override
            public void log(OperationLogEntry operationLogEntry) {
                operationLogServiceStatistics.append(operationLogEntry);
            }
        };
    }

    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.initialize();
        return executor;
    }

    @Bean
    @Primary
    @Autowired
    public PrintTemplatesClient printTemplatesClient(MicroServiceTemplate microServiceTemplate) {
        return new PrintTemplatesClientStub(microServiceTemplate);
    }
}