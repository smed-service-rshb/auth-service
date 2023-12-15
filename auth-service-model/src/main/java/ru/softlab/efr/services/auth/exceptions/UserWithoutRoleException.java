package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее отсутствие ролей у клиента
 *
 * @author akrenev
 * @since 13.03.2018
 */
public class UserWithoutRoleException extends AuthServiceException {
    /**
     * Конструктор
     */
    public UserWithoutRoleException(){
        super();
    }

    /**
     * Конструктор
     * @param message сообщение
     */
    public UserWithoutRoleException(String message){
        super(message);
    }

    /**
     * Конструктор
     * @param e исходное исключение
     */
    public UserWithoutRoleException(Exception e){
        super(e);
    }

    /**
     * Конструктор
     *
     * @param message сообщение
     * @param e исходное исключение
     */
   public UserWithoutRoleException(String message, Exception e){
        super(message, e);
    }
}
