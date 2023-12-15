package ru.softlab.efr.services.auth.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.softlab.efr.services.auth.exceptions.ConnectActiveDirectoryException;
import ru.softlab.efr.services.auth.exceptions.EmployeeGroupNotFoundException;
import ru.softlab.efr.services.auth.exceptions.TimeoutActiveDirectoryException;
import ru.softlab.efr.services.auth.exceptions.UserOfficeNotFoundException;
import ru.softlab.efr.services.auth.exchange.model.ErrorData;
import ru.softlab.efr.services.auth.exchange.model.ErrorDataErrors;

import javax.persistence.EntityExistsException;
import java.util.List;

import static ru.softlab.efr.services.auth.controllers.Utils.buildErrorRes;

/**
 * Обработчик исключений не контролируемых контроллером
 *
 * @author akrenev
 * @since 21.02.2018
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ORGUNIT_ID_FIELD_NAME = "orgUnitId";
    private static final String GROUP_ID_FIELD_NAME = "groupIds";

    @ExceptionHandler
    ResponseEntity handleException(final Throwable exception) {
        logger.error("Ошибка обработки запроса", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler
    ResponseEntity handleException(final TimeoutActiveDirectoryException exception) {
        logger.error("Таймаут подключения к AD.", exception);
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
    }

    @ExceptionHandler
    ResponseEntity handleException(final ConnectActiveDirectoryException exception) {
        logger.error("Ошибка соединения с AD.", exception);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        logger.error("Ошибка валидации.", ex);
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        ErrorData errorData = processFieldErrors(fieldErrors);
        return handleExceptionInternal(ex, errorData, headers, HttpStatus.BAD_REQUEST, request);
    }

    private ErrorData processFieldErrors(List<FieldError> fieldErrors) {
        ErrorData dto = new ErrorData();
        for (FieldError fieldError: fieldErrors) {
            ErrorDataErrors error = new ErrorDataErrors(fieldError.getField(), fieldError.getDefaultMessage());
            dto.addErrorsItem(error);
        }
        return dto;
    }

    @ExceptionHandler
    ResponseEntity handleException(final UserOfficeNotFoundException exception) {
        logger.error("Ошибка обработки запроса: некорректный id орг.структуры", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorRes(ORGUNIT_ID_FIELD_NAME, exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity handleException(final EmployeeGroupNotFoundException exception) {
        logger.error("Ошибка обработки запроса: некорректный id группы пользователя", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorRes(GROUP_ID_FIELD_NAME, exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity handleException(final EntityExistsException exception) {
        logger.error("Ошибка обработки запроса: сущность с данными параметрами уже присутствует в базе", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorRes("", exception.getMessage()));
    }
}
