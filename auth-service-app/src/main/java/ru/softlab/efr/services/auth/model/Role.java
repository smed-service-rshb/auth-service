package ru.softlab.efr.services.auth.model;

import ru.softlab.efr.services.auth.Right;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность, ассоциированная с БД таблицей "roles", которая хранит роли
 *
 * @author niculichev
 * @since 19.04.2017
 */
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ElementCollection(targetClass = Right.class, fetch = FetchType.EAGER)
    @JoinTable(name = "rights_to_roles", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @Column(name = "right_id", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Right> rights;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                '}';
    }
}
