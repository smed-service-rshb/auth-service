package ru.softlab.efr.services.auth.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.softlab.efr.common.settings.annotations.EnableSettings;
import ru.softlab.efr.common.utilities.hibernate.annotations.EnableHibernateJpa;
import ru.softlab.efr.infrastructure.logging.annotations.EnableLogging;
import ru.softlab.efr.infrastructure.transport.annotations.EnableTransport;
import ru.softlab.efr.services.auth.AuthServiceInfo;
import ru.softlab.efr.services.authorization.annotations.EnablePermissionControl;

/**
 * Основная конфигурация для всего приложения
 *
 * @author niculichev
 * @since 10.04.2017
 */
@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan(
        basePackages = {
                "ru.softlab.efr.services.auth",
                "ru.softlab.efr.common.client"
        })
@EnableHibernateJpa(
        repositoryPackages = "ru.softlab.efr.services.auth.services.impl.dao.repositories",
        modelPackages = "ru.softlab.efr.services.auth.model")
@EnablePermissionControl
@EnableTransport(AuthServiceInfo.SERVICE_NAME)
@EnableAsync
@EnableScheduling
@EnableSettings
@EnableLogging
public class ApplicationConfig {

    /**
     * @return bean с ресурсами
     */
    @Bean(name = "messages")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        return messageSource;
    }

    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.initialize();
        return executor;
    }
}
