package ru.softlab.efr.services.auth.services;

import ru.softlab.efr.services.auth.model.Right;

import java.util.List;

/**
 * Сервис хранилища прав
 *
 * @author akrenev
 * @since 15.02.2018
 */
public interface RightStoreService {

    /**
     * Получить полный список доступных прав
     *
     * @return список доступных прав
     */
    List<Right> getAll();
}
