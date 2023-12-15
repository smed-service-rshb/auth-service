package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее отсутствие информации о типе орг. структуры
 */
public class OrgUnitTypeNotFoundException extends AuthServiceException {
    /**
     * Конструктор
     */
    public OrgUnitTypeNotFoundException(){
        super();
    }

    /**
     * Конструктор
     * @param message сообщение
     */
    public OrgUnitTypeNotFoundException(String message){
        super(message);
    }

    /**
     * Конструктор
     * @param e исходное исключение
     */
    public OrgUnitTypeNotFoundException(Exception e){
        super(e);
    }

    /**
     * Конструктор
     *
     * @param message сообщение
     * @param e исходное исключение
     */
    public OrgUnitTypeNotFoundException(String message, Exception e){
        super(message, e);
    }
}
