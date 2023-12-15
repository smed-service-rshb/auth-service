package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.softlab.efr.services.auth.exchange.model.MotivationCorrectStatus;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.MotivationEntity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class MotivationEmployeeSearchRepository {

    private EntityManager em;

    public MotivationEmployeeSearchRepository(EntityManager em) {
        this.em = em;
    }

    public Page<Employee> getFilteredRequestsRq(Pageable pageable,
                                                final String surName, final String firstName, final String middleName,
                                                String personnelNumber, List<Long> orgUnitIdes, List<String> branches,
                                                String position, Long roleId, MotivationCorrectStatus motivationCorrectStatusm) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MotivationEntity> cq = cb.createQuery(MotivationEntity.class);

        Root<MotivationEntity> motivation = cq.from(MotivationEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(motivation.get("isActive"), true));
        predicates.add(cb.equal(motivation.get("motivationCorrectStatus"), motivationCorrectStatusm));

        Join<MotivationEntity, Employee> employeeJoin = motivation.join("user");
        if (Objects.nonNull(branches) && !branches.isEmpty()) {
            predicates.add(employeeJoin.join("orgUnits").get("parent").get("name").in(branches));
        }
        if (Objects.nonNull(orgUnitIdes) && !orgUnitIdes.isEmpty()) {
            predicates.add(employeeJoin.join("orgUnits").get("id").in(orgUnitIdes));
        }
        if (Objects.nonNull(roleId)) {
            predicates.add(employeeJoin.join("roles").get("id").in(roleId));
        }
        predicates.add(cb.notEqual(employeeJoin.get("deleted"), true));
        if (!StringUtils.isEmpty(surName)) {
            predicates.add(cb.like(cb.upper(employeeJoin.get("secondName")), surName.toUpperCase().trim() + '%'));
        }
        if (!StringUtils.isEmpty(firstName)) {
            predicates.add(cb.like(cb.upper(employeeJoin.get("firstName")), firstName.toUpperCase().trim() + '%'));
        }
        if (!StringUtils.isEmpty(middleName)) {
            predicates.add(cb.like(cb.upper(employeeJoin.get("middleName")), middleName.toUpperCase().trim() + '%'));
        }
        if (!StringUtils.isEmpty(personnelNumber)) {
            predicates.add(cb.equal(cb.upper(employeeJoin.get("personnelNumber")), personnelNumber.toUpperCase().trim()));
        }
        if (!StringUtils.isEmpty(position)) {
            predicates.add(cb.like(cb.upper(employeeJoin.get("position")), '%' + position.toUpperCase().trim() + '%'));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        List<MotivationEntity> motivationList = em.createQuery(cq).getResultList();

        List<Employee> responseList = motivationList.stream()
                .map(MotivationEntity::getUser)
                .collect(Collectors.toList());

        int start = pageable.getOffset();
        int end = (start + pageable.getPageSize()) > responseList.size() ? responseList.size() : (start + pageable.getPageSize());
        return new PageImpl<>(responseList.subList(start, end), pageable, responseList.size());
    }

}
