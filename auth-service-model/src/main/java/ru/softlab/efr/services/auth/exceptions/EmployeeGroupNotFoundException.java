package ru.softlab.efr.services.auth.exceptions;

public class EmployeeGroupNotFoundException extends AuthServiceException {
    /**
     * Конструктор
     */
    public EmployeeGroupNotFoundException(){
        super();
    }

    /**
     * Конструктор
     * @param message сообщение
     */
    public EmployeeGroupNotFoundException(String message){
        super(message);
    }

    /**
     * Конструктор
     * @param e исходное исключение
     */
    public EmployeeGroupNotFoundException(Exception e){
        super(e);
    }

    /**
     * Конструктор
     *
     * @param message сообщение
     * @param e исходное исключение
     */
    public EmployeeGroupNotFoundException(String message, Exception e){
        super(message, e);
    }
}
