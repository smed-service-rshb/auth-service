package ru.softlab.efr.services.auth.services;

import ru.softlab.efr.services.auth.model.Employee;

import java.util.List;

/**
 * Сервис работы с БД в разрезе пользователей организации
 *
 * @author prihodskiy
 * @since 10.08.2018
 */
public interface UserStoreService {

    /**
     * Получить всех сотрудников
     *
     * @return все сотрудники
     */
    List<Employee> getAll();

    /**
     * Получить сотрудника по идентификатору
     *
     * @param id идентификатор
     * @return сотрудник
     */
    Employee getById(Long id);

    /**
     * Получить не удалённого сотрудника по идентификатору
     *
     * @param id идентификатор
     * @return сотрудник
     */
    Employee getByIdNotDeleted(Long id);

    /**
     * Получить сотрудника по логину
     *
     * @param login логин
     * @return список сущностей сотрудников
     */
    Employee getByLogin(String login);

    /**
     * Получить полный список пользователей с учетом флага удаления
     * @param deleted false-только живые пользователи  true-только удаленные  null - все
     * @return список пользователей
     */
    List<Employee> getAll(Boolean deleted);
}
