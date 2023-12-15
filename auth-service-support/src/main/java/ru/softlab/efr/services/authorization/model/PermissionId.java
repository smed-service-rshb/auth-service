package ru.softlab.efr.services.authorization.model;

import ru.softlab.efr.services.auth.Right;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Objects;

/**
 * Идентификатор разрешения
 *
 * @author akrenev
 * @since 21.02.2018
 */
@Embeddable
public class PermissionId implements Serializable {
    private static final long serialVersionUID = -576494454770497501L;

    @Column(name = "permission_id")
    private String name;
    @Column(name = "right_id")
    @Enumerated(EnumType.STRING)
    private Right right;

    /**
     * Конструктор
     */
    public PermissionId() {
    }

    /**
     * Конструктор
     *
     * @param name  разрешение
     * @param right право
     */
    public PermissionId(String name, Right right) {
        this.name = name;
        this.right = right;
    }

    /**
     * Получить разрешение
     *
     * @return разрешение
     */
    public String getName() {
        return name;
    }

    /**
     * Задать разрешение
     *
     * @param name разрешение
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получить право
     *
     * @return право
     */
    public Right getRight() {
        return right;
    }

    /**
     * Задать право
     *
     * @param right право
     */
    public void setRight(Right right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PermissionId that = (PermissionId) o;
        return Objects.equals(name, that.name) &&
                right == that.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, right);
    }
}
