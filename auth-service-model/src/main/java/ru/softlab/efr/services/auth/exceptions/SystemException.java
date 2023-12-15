package ru.softlab.efr.services.auth.exceptions;

/**
 * Базовый класс системных исключений
 *
 * @author niculichev
 * @since 28.04.2017
 */
public class SystemException extends AuthServiceException {
    public SystemException(){
        super();
    }

    public SystemException(Exception e){
        super(e);
    }

    public SystemException(String message){
        super(message);
    }

    public SystemException(String message, Exception e){
        super(message, e);
    }
}
