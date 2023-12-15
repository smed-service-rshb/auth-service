package ru.softlab.efr.services.auth.services;

import ru.softlab.efr.services.auth.model.Role;

import java.util.Collection;
import java.util.List;

/**
 * Серсис работы с БД в разрезе ролей
 *
 * @author niculichev
 * @since 19.04.2017
 */
public interface RoleStoreService {

    /**
     * Получить все существующие роли
     *
     * @return все существующие роли
     */
    List<Role> getAll();

    /**
     * Получить роли по именам
     *
     * @param names уникальные имена ролей
     * @return список сущностей ролей
     */
    List<Role> getAllByNames(Collection<String> names);

    /**
     * Сохранить роль
     *
     * @param role сущность роли
     * @return идентификатор роли в БД
     */
    Long save(Role role);

    /**
     * Обновить роль
     *
     * @param role сущность роли
     * @return количество обновленных записей
     */
    int update(Role role);

    /**
     * Получить роль по ее уникальному имени
     *
     * @param name имя роли
     * @return Сущность роли
     */
    Role getByName(String name);

    /**
     * Получить роль по внутреннему идентификатору
     *
     * @param id внутренний идентификатор
     * @return Сущность роли
     */
    Role getById(Long id);

    /**
     * Удалить роль по внутреннему идентификатору
     *
     * @param id внутренний идентификатор
     * @return количество удаленных записей
     */
    int deleteById(Long id);
}
