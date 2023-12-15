package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее отсутствие в справочнике роли у клиента
 *
 * @author akrenev
 * @since 13.03.2018
 */
public class UserRoleNotFoundException extends AuthServiceException {
    /**
     * Конструктор
     */
    public UserRoleNotFoundException(){
        super();
    }

    /**
     * Конструктор
     * @param message сообщение
     */
    public UserRoleNotFoundException(String message){
        super(message);
    }

    /**
     * Конструктор
     * @param e исходное исключение
     */
    public UserRoleNotFoundException(Exception e){
        super(e);
    }

    /**
     * Конструктор
     *
     * @param message сообщение
     * @param e исходное исключение
     */
    public UserRoleNotFoundException(String message, Exception e){
        super(message, e);
    }
}
