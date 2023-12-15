package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, сигрализирующее о проблемах коннекта к AD
 *
 * @author niculichev
 * @since 28.04.2017
 */
public class ConnectActiveDirectoryException extends AuthServiceException {
    public ConnectActiveDirectoryException(){
        super();
    }
    public ConnectActiveDirectoryException(String message){
        super(message);
    }

    public ConnectActiveDirectoryException(Exception e){
        super(e);
    }

    public ConnectActiveDirectoryException(String message, Exception e){
        super(message, e);
    }
}
