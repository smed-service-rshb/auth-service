package ru.softlab.efr.services.authorization.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.model.Permission;
import ru.softlab.efr.services.authorization.repositories.PermissionRepository;

import java.util.Collection;
import java.util.EnumSet;

/**
 * Серсис работы с БД в разрезе связок ролей и разрешений (PermissionLink)
 *
 * @author niculichev
 * @since 29.05.2017
 */
@Service
public class PermissionStoreService {
    @Autowired
    private PermissionRepository permissionRepository;

    /**
     * Запрос прав, включающих разрешене
     *
     * @param permission разрешене
     * @return права, включающие разрешене
     */
    public Collection<Right> getRights(String permission) {
        Collection<Right> result = EnumSet.noneOf(Right.class);
        for (Permission permissionToRightMapping : permissionRepository.findAll()) {
            if (permissionToRightMapping.getName().equals(permission)) {
                result.add(permissionToRightMapping.getRight());
            }
        }
        return result;
    }
}
