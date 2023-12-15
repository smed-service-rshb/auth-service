package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее ошибку идентификации пользователя при аутентфикации
 *
 * @author niculichev
 * @since 29.04.2017
 */
public class UserIdentificationException extends AuthServiceException {
    public UserIdentificationException(){
        super();
    }
    public UserIdentificationException(String message){
        super(message);
    }

    public UserIdentificationException(Exception e){
        super(e);
    }

    public UserIdentificationException(String message, Exception e){
        super(message, e);
    }
}
