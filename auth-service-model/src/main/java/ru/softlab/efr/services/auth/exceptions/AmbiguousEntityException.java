package ru.softlab.efr.services.auth.exceptions;

/**
 * Ислючение, обозначающее неоднозначность обрабатываемой сущности(например, дублирование сотдрудников по переданному идентификатору)
 *
 * @author niculichev
 * @since 29.04.2017
 */
public class AmbiguousEntityException extends AuthServiceException {
    /**
     * Конструткор
     *
     * @param message сообщение
     */
    public AmbiguousEntityException(String message) {
        super(message);
    }
}
