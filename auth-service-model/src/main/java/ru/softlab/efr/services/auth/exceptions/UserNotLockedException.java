package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее что пользователь не заблокирован (при попытке разблокировки)
 *
 * @author akrenev
 * @since 13.03.2018
 */
public class UserNotLockedException extends AuthServiceException {
    /**
     * Конструктор
     */
    public UserNotLockedException(){
        super();
    }

    /**
     * Конструктор
     * @param message сообщение
     */
    public UserNotLockedException(String message){
        super(message);
    }

    /**
     * Конструктор
     * @param e исходное исключение
     */
    public UserNotLockedException(Exception e){
        super(e);
    }

    /**
     * Конструктор
     *
     * @param message сообщение
     * @param e исходное исключение
     */
    public UserNotLockedException(String message, Exception e){
        super(message, e);
    }
}
