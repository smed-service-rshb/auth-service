package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.softlab.efr.services.auth.model.Employee;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Employee findById(Long id);
    Employee findTopByLogin(String login);

    Employee findByLogin(String login);

    Employee findByIdAndDeleted(Long id, boolean deleted);

    List<Employee> findEmployeesByOrgUnitsIdAndDeleted(Long orgUnitId, boolean deleted);

    Employee findEmployeeByPersonnelNumberAndOrgUnitsId(String personnelNumber, Long orgUnitId);

    List<Employee> findAllByDeleted(boolean deleted, Sort sort);

    Page<Employee> findAllByDeleted(boolean deleted, Pageable pageable);

    @Query("from Employee u where UPPER(u.secondName) = UPPER(:secondName) and UPPER(u.firstName) = UPPER(:firstName) and u.birthDate = :birthDate and u.mobilePhone = :mobilePhone and u.deleted = false")
    List<Employee> findAllByFilter(@Param("secondName") String secondName, @Param("firstName") String firstName, @Param("birthDate") Date birthDate, @Param("mobilePhone") String mobilePhone);

    @Query("from Employee u where UPPER(u.secondName) = UPPER(:secondName) and UPPER(u.firstName) = UPPER(:firstName) and u.mobilePhone = :mobilePhone and u.deleted = false")
    List<Employee> findAllByFilter(@Param("secondName") String secondName, @Param("firstName") String firstName, @Param("mobilePhone") String mobilePhone);
}
