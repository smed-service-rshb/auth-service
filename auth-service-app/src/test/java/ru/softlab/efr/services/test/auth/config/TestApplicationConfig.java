package ru.softlab.efr.services.test.auth.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.softlab.efr.infrastructure.transport.MicroServiceRequestInterceptor;
import ru.softlab.efr.services.auth.AuthServiceInfo;
import ru.softlab.efr.services.auth.config.ActiveDirectoryConfig;
import ru.softlab.efr.services.auth.config.ApplicationConfig;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.services.authorization.interceptors.ClientInterceptor;
import ru.softlab.efr.test.infrastructure.transport.rest.config.AbstractTestConfiguration;

import static ru.softlab.efr.services.test.auth.MockData.getPrincipalData;

/**
 * @author khudyakov
 * @since 25.07.2017
 */
@Configuration
@ComponentScan(basePackages = {"ru.softlab.efr.services.auth"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ApplicationConfig.class)})
@Import({ActiveDirectoryConfig.class, HibernateConfig.class})
public class TestApplicationConfig extends AbstractTestConfiguration {
    private static final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    @Override
    protected String getTestAppName() {
        return AuthServiceInfo.SERVICE_NAME;
    }

    @Bean(name = "messages")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(0);
        return messageSource;
    }

    @Bean(ClientInterceptor.BEAN_NAME)
    public MicroServiceRequestInterceptor microServiceRequestInterceptor() {
        return (httpRequest, bytes) -> {
            try {
                httpRequest.getHeaders().add(PermissionControlConfig.HTTP_HEAD, serializer.serialize(getPrincipalData()));
            } catch (Exception ignore) {
            }
        };
    }
}