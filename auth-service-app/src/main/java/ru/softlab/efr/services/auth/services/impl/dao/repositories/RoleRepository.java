package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.auth.model.Role;

/**
 * Репозиторий работы с сущностью Role
 *
 * @author niculichev
 * @since 22.04.2017
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("select r from ru.softlab.efr.services.auth.model.Role r where r.name = :name")
    Role getByName(@Param("name") String name);
}
