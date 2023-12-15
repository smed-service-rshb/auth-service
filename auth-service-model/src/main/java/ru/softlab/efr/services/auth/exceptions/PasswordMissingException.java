package ru.softlab.efr.services.auth.exceptions;

public class PasswordMissingException extends AuthServiceException {

    public PasswordMissingException(){
        super();
    }
    public PasswordMissingException(String message){
        super(message);
    }

    public PasswordMissingException(Exception e){
        super(e);
    }

    public PasswordMissingException(String message, Exception e){
        super(message, e);
    }

}
