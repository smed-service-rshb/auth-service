package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.services.auth.exchange.model.MotivationCorrectStatus;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.MotivationEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MotivationRepository extends JpaRepository<MotivationEntity, Long> {

    MotivationEntity getByUserAndIsActive(Employee user, Boolean active);

    MotivationEntity getById(Long id);

    List<MotivationEntity> getByUser(Employee user);

    @Query("Select motivation " +
            "from MotivationEntity motivation " +
            "where (motivation.isActive = :active) " +
            "and (motivation.startDate between :startCount and :endCount) " +
            "and (motivation.motivationCorrectStatus in :motivationCorrectStatuses)")
    List<MotivationEntity> getReportMotivations(@Param("active") boolean active,
                                                @Param("startCount") LocalDate startCount, @Param("endCount") LocalDate endCount,
                                                @Param("motivationCorrectStatuses") List<MotivationCorrectStatus> motivationCorrectStatuses);

}
