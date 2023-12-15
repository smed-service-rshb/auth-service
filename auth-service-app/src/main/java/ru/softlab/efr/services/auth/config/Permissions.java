package ru.softlab.efr.services.auth.config;

/**
 * Используемые сервисом разрешения
 *
 * @author niculichev
 * @since 25.05.2017
 */
public class Permissions {
    /**
     * Разрешение на получение списка ролей
     */
    public static final String GET_ROLES = "get-roles";

    /**
     * Разрешение на создание роли
     */
    public static final String CREATE_ROLE = "create-role";

    /**
     * Разрешение на получение данных конкретной роли
     */
    public static final String GET_ROLE = "get-role";

    /**
     * Разрешение на обновление данных конкретной роли
     */
    public static final String UPDATE_ROLE = "update-role";

    /**
     * Разрешение на удаление конкретной роли
     */
    public static final String DELETE_ROLE = "delete-role";

    /**
     * Разрешение на получение всех возможных прав системы
     */
    public static final String GET_RIGHTS = "get-rights";

    /**
     * Разрешение на получение списка всех учётных записей сотрудников
     */
    public static final String GET_EMPLOYEES = "get-employees";

    /**
     * Разрешение на получение учётной записи сотрудника
     */
    public static final String GET_EMPLOYEE = "get-employee";

    /**
     * Разрешение на создание учётной записи сотрудника
     */
    public static final String CREATE_EMPLOYEE = "create-employee";

    /**
     * Разрешение на обновление данных учётной записи сотрудника
     */
    public static final String UPDATE_EMPLOYEE = "update-employee";

    /**
     * Разрешение на удаление учётной записи сотрудника
     */
    public static final String DELETE_EMPLOYEE = "delete-employee";

    /**
     * Разрешение на блокировку учётной записи сотрудника
     */
    public static final String LOCK_EMPLOYEE = "lock-employee";

    /**
     * Разрешение на разблокировку учётной записи сотрудника
     */
    public static final String UNLOCK_EMPLOYEE = "unlock-employee";

    /**
     * Разрешение на смену пароля для учётной записи сотрудника
     */
    public static final String RESET_PASSWORD = "reset-password";

    /**
     * Разрешение на создание и редактирование справочника групп пользователей
     */
    public static final String EDIT_EMPLOYEE_GROUPS = "edit-employee-groups";
}
