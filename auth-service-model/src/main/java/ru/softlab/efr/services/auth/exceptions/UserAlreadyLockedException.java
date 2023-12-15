package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее что пользователь уже заблокирован
 *
 * @author akrenev
 * @since 13.03.2018
 */
public class UserAlreadyLockedException extends AuthServiceException {
    /**
     * Конструктор
     */
    public UserAlreadyLockedException(){
        super();
    }

    /**
     * Конструктор
     * @param message сообщение
     */
    public UserAlreadyLockedException(String message){
        super(message);
    }

    /**
     * Конструктор
     * @param e исходное исключение
     */
    public UserAlreadyLockedException(Exception e){
        super(e);
    }

    /**
     * Конструктор
     *
     * @param message сообщение
     * @param e исходное исключение
     */
    public UserAlreadyLockedException(String message, Exception e){
        super(message, e);
    }
}
