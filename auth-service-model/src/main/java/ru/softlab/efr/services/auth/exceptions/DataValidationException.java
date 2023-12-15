package ru.softlab.efr.services.auth.exceptions;

import ru.softlab.efr.services.auth.exchange.ErrorsDataRs;

/**
 * Исключение, сигрализирующее об ошибках валидации
 *
 * @author niculichev
 * @since 29.04.2017
 */
public class DataValidationException extends AuthServiceException {
    private ErrorsDataRs errors;

    public DataValidationException(ErrorsDataRs errors){
        this.errors = errors;
    }

    public ErrorsDataRs getErrors() {
        return errors;
    }
}
