package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее что пользователь с указанным логином уже существует
 *
 * @author krivenko
 * @since 21.08.2018
 */
public class LoginAlreadyExistException extends AuthServiceException {

    public LoginAlreadyExistException(){
        super();
    }
    public LoginAlreadyExistException(String message){
        super(message);
    }

    public LoginAlreadyExistException(Exception e){
        super(e);
    }

    public LoginAlreadyExistException(String message, Exception e){
        super(message, e);
    }
}
