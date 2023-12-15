package ru.softlab.efr.services.auth.services;

import ru.softlab.efr.services.auth.model.Session;

/**
 * Серсис работы с БД в разрезе сессий
 *
 * @author niculichev
 * @since 20.04.2017
 */
public interface SessionStoreService {

    /**
     * Получить сущность сессии по идентификатору
     * @param uid уникальный идентфикатор сессии
     * @return Сущность сессии
     */
    Session getByUID(String uid);

    /**
     * Сохранить сущность сессии
     * @param session сущность сессии
     * @return идентфикатор сессии
     */
    String save(Session session);

    /**
     * Закрыть сессию
     * @param uid уникальный идентфикатор сессии
     * @return количество обновленных записей
     */
    int close(String uid);
}
