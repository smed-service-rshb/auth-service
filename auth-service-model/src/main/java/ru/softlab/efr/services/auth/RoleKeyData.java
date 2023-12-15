package ru.softlab.efr.services.auth;

import java.util.ArrayList;
import java.util.List;

/**
 * Ключевые данные по роли
 *
 * @author niculichev
 * @since 29.04.2017
 */
public class RoleKeyData {
    private String name;
    private List<Right> rights = new ArrayList<>();

    public RoleKeyData(){}

    public RoleKeyData(String name){
        this.name = name;
    }

    /**
     * @return уникальное наименование роли
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить доступные права для роли
     *
     * @return доступные права для роли
     */
    public List<Right> getRights() {
        return rights;
    }

    /**
     * Задать доступные права для роли
     *
     * @param rights доступные права для роли
     */
    public void setRights(List<Right> rights) {
        this.rights = rights;
    }
}
