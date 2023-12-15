package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softlab.efr.services.auth.model.EmployeeGroup;

public interface EmployeeGroupRepository extends JpaRepository<EmployeeGroup, Long> {

    EmployeeGroup findByCode(String code);

    EmployeeGroup findById(Long id);
}
