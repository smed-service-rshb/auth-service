package ru.softlab.efr.services.testapps.someservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Обработчик исключений
 */
@RestControllerAdvice
public class SomeExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    ResponseEntity handleException(final Throwable exception) {
        logger.error("Ошибка обработки запроса", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @ExceptionHandler
    ResponseEntity handleException(final SomeException exception) {
        logger.error("Ошибка обработки запроса", exception);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exception.getMessage());
    }
}
