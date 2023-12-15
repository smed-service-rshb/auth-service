package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее ошибку аутентификации, т.е. неверные credentials
 *
 * @author niculichev
 * @since 28.04.2017
 */
public class UserAuthenticationException extends AuthServiceException {
    public UserAuthenticationException(){
        super();
    }
    public UserAuthenticationException(String message){
        super(message);
    }

    public UserAuthenticationException(Exception e){
        super(e);
    }

    public UserAuthenticationException(String message, Exception e){
        super(message, e);
    }
}
