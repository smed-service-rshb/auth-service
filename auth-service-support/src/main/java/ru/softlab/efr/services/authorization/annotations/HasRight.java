package ru.softlab.efr.services.authorization.annotations;

import ru.softlab.efr.services.auth.Right;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для проверки прав. Данная аннотация ставиться как метаданные веб-метода контролера, что является
 * критерием для проверки прав перед вызовом метода (см ServerInterceptor).
 *
 * @author akrenev
 * @since 26.02.2018
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HasRight {
    /**
     * Получить имя права
     *
     * @return имя права
     */
    Right value();
}
