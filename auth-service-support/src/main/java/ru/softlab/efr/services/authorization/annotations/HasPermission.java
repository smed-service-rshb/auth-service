package ru.softlab.efr.services.authorization.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для проверки разрешений. Данная аннотация ставиться как метаданные веб-метода контролера, что является
 * критерием для проверки прав перед вызовом метода (см ServerInterceptor).
 *
 * @author niculichev
 * @since 19.05.2017
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HasPermission {
    /**
     * @return имя разрешения
     */
    @AliasFor("and")
    String[] value() default {};

    /**
     * дизъюнкция разрешений
     */
    String[] or() default {};

    /**
     * конъюнкция разрешений
     */
    @AliasFor("value")
    String[] and() default {};
}
