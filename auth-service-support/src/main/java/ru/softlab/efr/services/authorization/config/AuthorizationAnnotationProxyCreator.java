package ru.softlab.efr.services.authorization.config;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import ru.softlab.efr.services.authorization.SecurityContext;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;

import java.lang.reflect.Method;

/**
 * Обработчик аннотаций HasPermission и HasRight
 * Создает прокси с проверкой полномочий перед вызовом метода для классов, которые используют данные аннотации.
 */
@Component
public class AuthorizationAnnotationProxyCreator extends AbstractAutoProxyCreator {
    private static final long serialVersionUID = -4017041427569894859L;

    private final transient Object[] advices;

    /**
     * Конструктор
     *
     * @param securityContext Контекс безопасности
     */
    public AuthorizationAnnotationProxyCreator(SecurityContext securityContext) {
        this.advices = new Object[]{new AuthorizationMethodInterceptor(securityContext)};
    }

    @Override
    protected boolean shouldProxyTargetClass(Class<?> beanClass, String beanName) {
        return true;
    }

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
        Class<?> type = ClassUtils.getUserClass(beanClass);
        Method[] methods = type.getMethods();
        for (Method method : methods) {
            if (AnnotationUtils.findAnnotation(method, HasPermission.class) != null) {
                return advices;
            }
            if (AnnotationUtils.findAnnotation(method, HasRight.class) != null) {
                return advices;
            }
        }
        return DO_NOT_PROXY;
    }
}
