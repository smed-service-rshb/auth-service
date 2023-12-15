package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.auth.model.Session;

/**
 * Репозиторий работы с сущностью Session
 *
 * @author niculichev
 * @since 22.04.2017
 */
public interface SessionRepository extends JpaRepository<Session, String> {
    @Modifying
    @Transactional
    @Query("update ru.softlab.efr.services.auth.model.Session s set s.state = ru.softlab.efr.services.auth.model.Session$State.closed where s.uid = :uid")
    int close(@Param("uid") String name);

    Session getFirstByOwnerIdOrderByCreationDate(Long ownerId);

}

