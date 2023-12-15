package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее timeout при работе с AD
 *
 * @author niculichev
 * @since 28.04.2017
 */
public class TimeoutActiveDirectoryException extends AuthServiceException {
    public TimeoutActiveDirectoryException(){
        super();
    }
    public TimeoutActiveDirectoryException(String message){
        super(message);
    }

    public TimeoutActiveDirectoryException(Exception e){
        super(e);
    }

    public TimeoutActiveDirectoryException(String message, Exception e){
        super(message, e);
    }
}
