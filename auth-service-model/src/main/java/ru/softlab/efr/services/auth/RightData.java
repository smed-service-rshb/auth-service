package ru.softlab.efr.services.auth;

/**
 * Данные о праве
 *
 * @author akrenev
 * @since 15.02.2018
 */
public class RightData {
    private Right externalId;
    private String description;

    /**
     * Получить внешний идентификатор
     *
     * @return идентификатор
     */
    public Right getExternalId() {
        return externalId;
    }

    /**
     * Задать внешний идентификатор
     *
     * @param externalId идентификатор
     */
    public void setExternalId(Right externalId) {
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
