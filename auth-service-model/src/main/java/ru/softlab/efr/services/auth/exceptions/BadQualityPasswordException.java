package ru.softlab.efr.services.auth.exceptions;

import ru.softlab.efr.services.auth.exceptions.AuthServiceException;

import java.util.List;

public class BadQualityPasswordException extends AuthServiceException {

    private List<String> errors;

    public BadQualityPasswordException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
