package ru.softlab.efr.services.authorization.config;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.core.annotation.AnnotationUtils;
import ru.softlab.efr.services.authorization.ForbiddenException;
import ru.softlab.efr.services.authorization.SecurityContext;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;

import java.lang.reflect.Method;

class AuthorizationMethodInterceptor implements MethodBeforeAdvice {
    private SecurityContext securityContext;

    AuthorizationMethodInterceptor(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @Override
    public void before(Method handlerMethod, Object[] args, Object target) {

        HasPermission hasPermission = AnnotationUtils.findAnnotation(handlerMethod, HasPermission.class);
        HasRight hasRight = AnnotationUtils.findAnnotation(handlerMethod, HasRight.class);

        String methodName = handlerMethod.getName();
        if (permissionDenied(hasPermission, methodName) || rightDenied(hasRight)) {
            throw new ForbiddenException();
        }
    }

    private boolean permissionDenied(HasPermission hasPermission, String methodName) {
        return hasPermission != null && !hasPermission(hasPermission, methodName);
    }

    private boolean hasPermission(HasPermission hasPermission, String methodName) {
        if (ArrayUtils.isNotEmpty(hasPermission.and())) {
            for (String permission : hasPermission.and()) {
                if (!securityContext.implies(permission)) {
                    return false;
                }
            }
            return true;
        }

        if (ArrayUtils.isNotEmpty(hasPermission.or())) {
            for (String permission : hasPermission.or()) {
                if (securityContext.implies(permission)) {
                    return true;
                }
            }
            return false;
        }

        throw new IllegalArgumentException("Неверные параметры аннотации HasPermission у метода " + methodName);
    }

    private boolean rightDenied(HasRight hasRight) {
        return hasRight != null && !hasRight(hasRight);
    }

    private boolean hasRight(HasRight hasRight) {
        return securityContext.implies(hasRight.value());
    }

}
