package ru.softlab.efr.services.auth.model;

import javax.persistence.*;

/**
 * Право (минимальная единица управления правами доступа)
 *
 * @author akrenev
 * @since 14.02.2018
 */
@Entity
@Table(name = "rights")
public class Right {
    @Id
    @Enumerated(EnumType.STRING)
    private ru.softlab.efr.services.auth.Right externalId;
    private String description;

    /**
     * Получить внешний идентификатор
     *
     * @return идентификатор
     */
    public ru.softlab.efr.services.auth.Right getExternalId() {
        return externalId;
    }

    /**
     * Задать внешний идентификатор
     *
     * @param externalId идентификатор
     */
    public void setExternalId(ru.softlab.efr.services.auth.Right externalId) {
        this.externalId = externalId;
    }

    /**
     * Получить описание
     *
     * @return описание
     */
    public String getDescription() {
        return description;
    }

    /**
     * Задать описание
     *
     * @param description описание
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
