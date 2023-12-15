package ru.softlab.efr.services.auth.controllers;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import ru.softlab.efr.services.auth.ErrorData;
import ru.softlab.efr.services.auth.exchange.ErrorsDataRs;


import java.lang.reflect.InvocationTargetException;

/**
 * Класс с утилитными методами для контроллеров
 *
 * @author niculichev
 * @since 20.04.2017
 */
class Utils {

    /**
     * Сформировать тело ответа с ошибкой
     *
     * @param errors ошибки
     * @return тело ответа с ошибкой
     */
    static ErrorsDataRs buildErrorRes(Errors errors) {
        ErrorsDataRs rs = new ErrorsDataRs();
        for (ObjectError error : errors.getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                rs.getErrors().add(new ErrorData(fieldError.getField(), fieldError.getDefaultMessage()));
            } else {
                rs.getErrors().add(new ErrorData(null, "binding error"));
            }
        }

        return rs;
    }

    /**
     * Сформировать тело ответа с ошибкой
     *
     * @param field   поле, содержащее ошибку
     * @param message сообщение об ошибке
     * @return тело ответа с ошибкой
     */
    static ErrorsDataRs buildErrorRes(String field, String message) {
        ErrorsDataRs rs = new ErrorsDataRs();
        rs.getErrors().add(new ErrorData(field, message));
        return rs;
    }


    public static Object mapSimilarObjects(Object source, Object dest) {
        try {
            BeanUtils.copyProperties(dest, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Can't copy field to " + dest.getClass().getSimpleName(), e);
        }
        return dest;
    }
}
