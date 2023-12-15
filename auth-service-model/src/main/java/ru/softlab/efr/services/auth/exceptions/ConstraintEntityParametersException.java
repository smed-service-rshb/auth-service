package ru.softlab.efr.services.auth.exceptions;

/**
 * Сработало ограничениа на параметры(например уникальность имени и т.п.)
 *
 * @author niculichev
 * @since 29.04.2017
 */
public class ConstraintEntityParametersException extends AuthServiceException {
    public ConstraintEntityParametersException(){
        super();
    }
    public ConstraintEntityParametersException(String message){
        super(message);
    }

    public ConstraintEntityParametersException(Exception e){
        super(e);
    }

    public ConstraintEntityParametersException(String message, Exception e){
        super(message, e);
    }
}
