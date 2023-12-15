package ru.softlab.efr.services.auth.exchange;

import ru.softlab.efr.services.auth.RoleKeyData;

import java.util.Collection;

/**
 * Ответ на запрос данных по ролям
 *
 * @author akrenev
 * @since 20.02.2018
 */
public class GetRolesDataRs {
    private Collection<RoleKeyData> rolesData;

    /**
     * Конструктор
     */
    public GetRolesDataRs() {
    }

    /**
     * Конструктор
     *
     * @param rolesData информация о ролях
     */
    public GetRolesDataRs(Collection<RoleKeyData> rolesData) {
        this.rolesData = rolesData;
    }

    /**
     * Получить информацию о ролях
     *
     * @return информация о ролях
     */
    public Collection<RoleKeyData> getRolesData() {
        return rolesData;
    }

    /**
     * Задать информацию о ролях
     *
     * @param rolesData информация о ролях
     */
    public void setRolesData(Collection<RoleKeyData> rolesData) {
        this.rolesData = rolesData;
    }
}
