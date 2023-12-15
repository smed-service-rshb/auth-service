package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, сигрализирующее о неверных настройках AD
 *
 * @author niculichev
 * @since 29.04.2017
 */
public class ConfigurationException extends SystemException {
    public ConfigurationException(){
        super();
    }
    public ConfigurationException(String message){
        super(message);
    }

    public ConfigurationException(Exception e){
        super(e);
    }

    public ConfigurationException(String message, Exception e){
        super(message, e);
    }
}
