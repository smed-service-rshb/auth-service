package ru.softlab.efr.services.authorization;

/**
 * Источник данных принципала
 *
 * @author niculichev
 * @since 25.05.2017
 */
public interface PrincipalDataSource {
    /**
     * @return данные принципала
     */
    PrincipalData getPrincipalData();
}
