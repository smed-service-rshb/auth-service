package ru.softlab.efr.services.testapps.someservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Некий контроллер
 */
@RestController
public class ApiController {
    private final Delegate delegate;

    ApiController(Delegate delegate) {

        this.delegate = delegate;
    }

    /**
     * Запрос для проверки прав в дочерних классах
     *
     * @param message сообщение об ошшибке
     * @return респонз
     */
    @GetMapping("/ApiController/{message}")
    public String superClassHandler(@PathVariable String message) {
        return delegate.doHandleRequest(message);
    }

    interface Delegate {
        /**
         * Обработать сообщение
         *
         * @param message сообщение
         * @return ответ
         */
        String doHandleRequest(String message);
    }
}
