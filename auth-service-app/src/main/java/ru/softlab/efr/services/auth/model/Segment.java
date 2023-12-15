package ru.softlab.efr.services.auth.model;

import javax.persistence.*;


/**
 * Сущность для хранения данных справочника сгементов.
 */
@Entity
@Table(name = "segment")
public class Segment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    /**
     * Код сегмента
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * Название сегмента
     */
    @Column(name = "name", nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
