package ru.softlab.efr.services.auth;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Данные по роли
 *
 * @author niculichev
 * @since 19.04.2017
 */
public class RoleData {

    private Long id;

    @NotEmpty(message = "{RoleData.name.NotEmpty}")
    private String name;

    @NotEmpty(message = "{RoleData.desc.NotEmpty}")
    private String desc;

    private List<Right> rights;

    /**
     * Конструктор
     */
    public RoleData() {
    }

    /**
     * Конструктор
     *
     * @param name   уникальное наименование роли
     * @param desc   описание роли
     * @param rights набор доступных прав
     */
    public RoleData(String name, String desc, List<Right> rights) {
        this.name = name;
        this.desc = desc;
        this.rights = rights;
    }

    /**
     * Получить уникальное наименование роли
     *
     * @return уникальное наименование роли
     */
    public String getName() {
        return name;
    }

    /**
     * Задать уникальное наименование роли
     *
     * @param name уникальное наименование роли
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить описание роли
     *
     * @return описание роли
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Задать описание роли
     *
     * @param desc описание роли
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Получить набор доступных прав
     *
     * @return набор доступных прав
     */
    public List<Right> getRights() {
        return rights;
    }

    /**
     * Задать набор доступных прав
     *
     * @param rights набор доступных прав
     */
    public void setRights(List<Right> rights) {
        this.rights = rights;
    }

    /**
     * Получить внутренний идентификатор роли
     *
     * @return внутренний идентификатор роли
     */
    public Long getId() {
        return id;
    }

    /**
     * Задать внутренний идентификатор роли
     *
     * @param id внутренний идентификатор роли
     */
    public void setId(Long id) {
        this.id = id;
    }
}
