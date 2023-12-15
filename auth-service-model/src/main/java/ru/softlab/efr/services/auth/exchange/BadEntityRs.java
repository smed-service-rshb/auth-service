package ru.softlab.efr.services.auth.exchange;

import ru.softlab.efr.services.auth.exceptions.LoginBlockException;
import ru.softlab.efr.services.auth.exceptions.UserOfficeNotFoundException;
import ru.softlab.efr.services.auth.exceptions.UserRoleNotFoundException;
import ru.softlab.efr.services.auth.exceptions.UserWithoutRoleException;

/**
 * Тело ответа для ошибок некорректного состяния сущности
 *
 * @author akrenev
 * @since 23.03.2018
 */
public class BadEntityRs {
    private Type type;
    private String message;

    /**
     * Конструтор
     */
    public BadEntityRs() {
    }

    private BadEntityRs(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Получить тело ответа для ошибки "пользователь заблокирован"
     *
     * @param exception исключение
     * @return тело ответа
     */
    public static BadEntityRs userBlockedResponse(LoginBlockException exception) {
        return new BadEntityRs(Type.BLOCKED, exception.getMessage());
    }

    /**
     * Получить тело ответа для ошибки "офис пользователя не определен"
     *
     * @param exception исключение
     * @return тело ответа
     */
    public static BadEntityRs userOfficeNotFoundResponse(UserOfficeNotFoundException exception) {
        return new BadEntityRs(Type.OFFICE_NOT_FOUND, exception.getMessage());
    }

    /**
     * Получить тело ответа для ошибки "роль пользователя не определена"
     *
     * @param exception исключение
     * @return тело ответа
     */
    public static BadEntityRs userRoleNotFoundExceptionResponse(UserRoleNotFoundException exception) {
        return new BadEntityRs(Type.ROLE_NOT_FOUND, exception.getMessage());
    }

    /**
     * Получить тело ответа для ошибки "отсутствуют роли у пользователя"
     *
     * @param exception исключение
     * @return тело ответа
     */
    public static BadEntityRs userWithoutRoleExceptionResponse(UserWithoutRoleException exception) {
        return new BadEntityRs(Type.EMPTY_ROLE, exception.getMessage());
    }

    /**
     * Получить тип ошибки
     *
     * @return тип ошибки
     */
    public Type getType() {
        return type;
    }

    /**
     * Задать тип ошибки
     *
     * @param type тип ошибки
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Получить сообщение
     *
     * @return сообщение
     */
    public String getMessage() {
        return message;
    }

    /**
     * Получить сообщение
     *
     * @param message сообщение
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Восстановить исключение
     *
     * @throws LoginBlockException         пользователь заблокирован
     * @throws UserOfficeNotFoundException офис пользователя не определен
     * @throws UserRoleNotFoundException   роль пользователя не определена
     * @throws UserWithoutRoleException    отсутствуют роли у пользователя
     */
    public void raise() throws LoginBlockException, UserOfficeNotFoundException, UserRoleNotFoundException, UserWithoutRoleException {
        switch (type) {
            case BLOCKED:
                throw new LoginBlockException(message);
            case OFFICE_NOT_FOUND:
                throw new UserOfficeNotFoundException(message);
            case ROLE_NOT_FOUND:
                throw new UserRoleNotFoundException(message);
            case EMPTY_ROLE:
                throw new UserWithoutRoleException(message);
        }
    }

    /**
     * Тип ошибки
     */
    public enum Type {
        /**
         * Пользователь заблокирован
         */
        BLOCKED,
        /**
         * Не определен офис пользователя
         */
        OFFICE_NOT_FOUND,
        /**
         * Не определена роль пользователя
         */
        ROLE_NOT_FOUND,
        /**
         * Не заданы роли пользователя
         */
        EMPTY_ROLE,
    }
}
