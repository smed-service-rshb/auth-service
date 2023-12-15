package ru.softlab.efr.services.authorization.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import ru.softlab.efr.common.utilities.hibernate.annotations.EnableHibernateJpa;
import ru.softlab.efr.services.authorization.annotations.EnablePermissionControl;
import ru.softlab.efr.services.authorization.interceptors.ClientInterceptor;

import java.util.Map;

/**
 * Регистратор бина для вставки соурса данных принципала относительно параметра аннотации
 *
 * @author niculichev
 * @since 22.05.2017
 */
public class AuthorizationBeansRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        if (!annotationMetadata.hasAnnotation(EnableHibernateJpa.class.getName())) {
            throw new RuntimeException("Для правильной работы прав необходимо подключить аннотацию [" + EnableHibernateJpa.class.getName() + "] к основной конфигурации.");
        }

        Map<String, Object> map = annotationMetadata.getAnnotationAttributes(EnablePermissionControl.class.getName());
        String sourceBeanRef = (String) map.get("source");

        BeanDefinition beanDefinitionClient = registry.getBeanDefinition(ClientInterceptor.BEAN_NAME);
        beanDefinitionClient.getConstructorArgumentValues()
                .addIndexedArgumentValue(0, new RuntimeBeanReference(sourceBeanRef));

    }
}
