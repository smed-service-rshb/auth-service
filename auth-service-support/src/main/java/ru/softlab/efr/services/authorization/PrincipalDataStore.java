package ru.softlab.efr.services.authorization;

/**
 * Хранилище данных принципала
 *
 * @author niculichev
 * @since 22.05.2017
 */
public interface PrincipalDataStore extends PrincipalDataSource {
    /**
     * Сохранить данные принципала
     * @param principalData данные
     */
    void setPrincipalData(PrincipalData principalData);

    /**
     * Очистка данных принципала
     */
    void clearPrincipalData();
}
