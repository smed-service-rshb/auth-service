package ru.softlab.efr.services.testapps.someservice.controllers;

/**
 * Некоторое исключение
 */
class SomeException extends RuntimeException {
    /**
     * Конструктор
     *
     * @param message сообщение
     */
    SomeException(String message) {
        super(message);
    }
}
