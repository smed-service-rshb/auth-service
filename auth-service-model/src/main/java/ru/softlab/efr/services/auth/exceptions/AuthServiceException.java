package ru.softlab.efr.services.auth.exceptions;

/**
 * Базоый класс для исключений auth-service
 *
 * @author niculichev
 * @since 29.04.2017
 */
public class AuthServiceException extends Exception {
    public AuthServiceException(){
        super();
    }
    public AuthServiceException(String message){
        super(message);
    }

    public AuthServiceException(Exception e){
        super(e);
    }

    public AuthServiceException(String message, Exception e){
        super(message, e);
    }
}
