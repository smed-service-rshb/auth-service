package ru.softlab.efr.services.auth.exceptions;

/**
 * Исключение, обозначающее отсутствии разрешений на выполняемую операцию
 *
 * @author niculichev
 * @since 29.04.2017
 */
public class PermissionDeniedException extends AuthServiceException {
    /**
     * Конструктор
     */
    public PermissionDeniedException() {
        super();
    }

    /**
     * Конструктор
     *
     * @param message сообщение
     */
    public PermissionDeniedException(String message) {
        super(message);
    }

    /**
     * Конструктор
     *
     * @param e исходное исключение
     */
    public PermissionDeniedException(Exception e) {
        super(e);
    }

    /**
     * Конструктор
     *
     * @param message сообщение
     * @param e       исходное исключение
     */
    public PermissionDeniedException(String message, Exception e) {
        super(message, e);
    }
}
