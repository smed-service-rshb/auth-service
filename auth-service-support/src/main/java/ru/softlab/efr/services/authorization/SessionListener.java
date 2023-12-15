package ru.softlab.efr.services.authorization;

/**
 * Листенер обработки событий, связанных с сессией
 *
 * @author niculichev
 * @since 29.05.2017
 */
public interface SessionListener {
    /**
     * Закрытие сессиии
     * @param event информация по событию
     */
    void handle(SessionEvent event);
}
