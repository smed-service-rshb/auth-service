package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softlab.efr.services.auth.model.CurrentState;

/**
 * Репозиторий для работы с сущностью статусов выполнения операций
 *
 * @author olshansky
 * @since 21.09.2018.
 */
public interface CurrentStateRepository extends JpaRepository<CurrentState, Long> {

    CurrentState findTopByNameOrderByModifiedDesc(String name);
    CurrentState findTopByNameAndIsFinishedOrderByIdDesc(String name, Boolean isFinished);
}
