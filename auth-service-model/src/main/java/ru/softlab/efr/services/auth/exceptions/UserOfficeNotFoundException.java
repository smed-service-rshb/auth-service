package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее отсутствие информации о подразделении у клиента
 *
 * @author akrenev
 * @since 13.03.2018
 */
public class UserOfficeNotFoundException extends AuthServiceException {
    /**
     * Конструктор
     */
    public UserOfficeNotFoundException(){
        super();
    }

    /**
     * Конструктор
     * @param message сообщение
     */
    public UserOfficeNotFoundException(String message){
        super(message);
    }

    /**
     * Конструктор
     * @param e исходное исключение
     */
    public UserOfficeNotFoundException(Exception e){
        super(e);
    }

    /**
     * Конструктор
     *
     * @param message сообщение
     * @param e исходное исключение
     */
    public UserOfficeNotFoundException(String message, Exception e){
        super(message, e);
    }
}
