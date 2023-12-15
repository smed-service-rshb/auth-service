package ru.softlab.efr.services.authorization;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.services.PermissionStoreService;

import java.util.Collection;
import java.util.function.Function;

/**
 * Контекс безопасности, по сути класс, содержащий метода для проверки прав
 *
 * @author niculichev
 * @since 31.05.2017
 */
public class SecurityContext {
    private static final Log LOG = LogFactory.getLog(SecurityContext.class);

    private PrincipalDataSource principalDataSource;
    private PermissionStoreService permissionService;

    /**
     * конструктор
     * @param permissionStoreService Серсис для работы разрешениями
     * @param principalDataSource Источник данных принципала
     */
    SecurityContext(PermissionStoreService permissionStoreService, PrincipalDataSource principalDataSource) {
        this.permissionService = permissionStoreService;
        this.principalDataSource = principalDataSource;
    }

    /**
     * Проверка разрешения для текущего принципала
     *
     * @param permissionName наименование разрешения
     * @return true - принципал имеет данное разрешение
     */
    public boolean implies(String permissionName) {
        return implies(principalRights -> {
            Collection<Right> serviceRights = permissionService.getRights(permissionName);
            return CollectionUtils.containsAny(serviceRights, principalRights);
        });
    }

    /**
     * Получить признак наличия права у принципала
     *
     * @param right проверяемое право
     * @return признак наличия права
     */
    public boolean implies(Right right) {
        return implies(principalRights -> principalRights.contains(right));
    }

    private boolean implies(Function<Collection<Right>, Boolean> checker) {
        PrincipalData principalData = principalDataSource.getPrincipalData();
        if (principalData == null) {
            LOG.warn("Отсутствует контекс с данными принципала.");
            return false;
        }

        Collection<Right> principalRights = principalData.getRights();
        return checker.apply(principalRights);
    }
}
