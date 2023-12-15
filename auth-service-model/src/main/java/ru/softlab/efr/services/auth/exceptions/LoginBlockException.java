package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее блокировку конкретного логина
 *
 * @author niculichev
 * @since 28.04.2017
 */
public class LoginBlockException extends AuthServiceException {
    public LoginBlockException(){
        super();
    }
    public LoginBlockException(String message){
        super(message);
    }

    public LoginBlockException(Exception e){
        super(e);
    }

    public LoginBlockException(String message, Exception e){
        super(message, e);
    }
}
