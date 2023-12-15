package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.UserLoginAttempt;

import java.time.LocalDateTime;

public interface UserLoginAttemptsRepository extends JpaRepository<UserLoginAttempt, Long> {

    void deleteAllByUser(Employee user);

    Integer countAllByUserAndCreationDateAfter(Employee user, LocalDateTime dateTimeFrom);

    Integer countAllByUser(Employee user);
}
