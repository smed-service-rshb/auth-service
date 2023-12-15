package ru.softlab.efr.services.authorization.repositories;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.softlab.efr.services.authorization.model.Permission;

import java.util.List;

import static ru.softlab.efr.services.authorization.config.PermissionControlConfig.PERMISSION_CACHE_MANAGER;
import static ru.softlab.efr.services.authorization.config.PermissionControlConfig.PERMISSION_TO_RIGHT_CACHE_NAME;

/**
 * Репозиторий разрешений
 *
 * @author akrenev
 * @since 20.02.2018
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    /**
     * Запрос полного отношения прав и разрешений
     *
     * @return права, включающие разрешене
     */
    @Cacheable(cacheNames = PERMISSION_TO_RIGHT_CACHE_NAME, cacheManager = PERMISSION_CACHE_MANAGER)
    @Override
    List<Permission> findAll();
}
