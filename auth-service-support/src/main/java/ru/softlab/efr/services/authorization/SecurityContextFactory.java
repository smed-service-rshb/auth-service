package ru.softlab.efr.services.authorization;

import ru.softlab.efr.services.authorization.services.PermissionStoreService;

/**
 * Фабрика для контекста безопасности
 * @author basharin
 * @since 21.06.2018
 */
public class SecurityContextFactory {

    private PermissionStoreService permissionStoreService;

    /**
     * конструктор
     * @param permissionStoreService Серсис для работы разрешениями
     */
    public SecurityContextFactory(PermissionStoreService permissionStoreService) {
        this.permissionStoreService = permissionStoreService;
    }

    /**
     * создать контекста безопасности
     * @param principalDataSource Источник данных принципала
     * @return контекста безопасности
     */
    public SecurityContext createContext(PrincipalDataSource principalDataSource) {
        return new SecurityContext(permissionStoreService, principalDataSource);
    }
}
