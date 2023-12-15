package ru.softlab.efr.services.auth.exchange;

import ru.softlab.efr.services.auth.RoleListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Тело ответа на зпрос получения списка всех ролей
 *
 * @author niculichev
 * @since 20.04.2017
 */
public class GetRolesRs {
    private List<RoleListData> roles = new ArrayList<>();

    public List<RoleListData> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleListData> roles) {
        this.roles = roles;
    }
}
