package ru.softlab.efr.services.authorization.model;

import ru.softlab.efr.services.auth.Right;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Моппинг пермишена на право
 *
 * @author akrenev
 * @since 20.02.2018
 */
@Entity
@Table(name = "rights_to_permission")
public class Permission implements Serializable {
    private static final long serialVersionUID = 4339778240061061997L;

    @EmbeddedId
    private PermissionId id;

    /**
     * Получить разрешение
     *
     * @return разрешение
     */
    public String getName() {
        return id.getName();
    }

    /**
     * Получить право
     *
     * @return право
     */
    public Right getRight() {
        return id.getRight();
    }
}
