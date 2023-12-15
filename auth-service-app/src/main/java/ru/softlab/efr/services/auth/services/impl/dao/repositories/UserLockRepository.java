package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import ru.softlab.efr.services.auth.model.UserLock;

import javax.persistence.QueryHint;
import java.util.List;

public interface UserLockRepository extends JpaRepository<UserLock, Long> {

    UserLock findFirstByUserIdAndLockedAndEndDateTimeIsNull(Long employeeId, boolean isLocked);

    @Query("select l from UserLock l " +
            "where l.locked = :isLocked " +
            "and l.user.id = :userId " +
            "and (l.endDateTime is null or l.endDateTime > CURRENT_TIMESTAMP)")
    List<UserLock> findAllByUserIdAndLocked(
            @Param("userId")Long employeeId,
            @Param("isLocked") boolean isLocked);
}
