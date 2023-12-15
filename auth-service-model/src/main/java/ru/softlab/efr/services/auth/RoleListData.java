package ru.softlab.efr.services.auth;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Данные по роли без разрешений(для списочных структур)
 *
 * @author niculichev
 * @since 27.04.2017
 */
public class RoleListData {
    private Long id;
    private String name;
    private String desc;

    public RoleListData(){
    }

    public RoleListData(Long id, String name, String desc){
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

    /**
     * @return внутренний идентфикатор роли
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
     * @return описание роли
     */
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
