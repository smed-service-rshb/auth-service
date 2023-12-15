package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import ru.softlab.efr.services.auth.model.Employee;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSearchSpecification {

    public static Specification<Employee> employeeSpecification(final String surName, final String firstName, final String middleName,
                                                                String personnelNumber, List<String> branches, List<Long> orgUnitIdes,
                                                                String position, Long roleId) {

        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            criteriaQuery.distinct(true);

            if ((branches != null) && !branches.isEmpty()) {
                predicates.add(root.join("orgUnits").get("parent").get("name").in(branches));
            }

            if ((orgUnitIdes != null) && !orgUnitIdes.isEmpty()) {
                predicates.add(root.join("orgUnits").get("id").in(orgUnitIdes));
            }

            if (roleId != null) {
                predicates.add(root.join("roles").get("id").in(roleId));
            }

            predicates.add(criteriaBuilder.notEqual(root.get("deleted"), true));

            if (!StringUtils.isEmpty(surName)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("secondName")), surName.toUpperCase().trim() + '%'));
            }

            if (!StringUtils.isEmpty(firstName)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("firstName")), firstName.toUpperCase().trim() + '%'));
            }

            if (!StringUtils.isEmpty(middleName)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("middleName")), middleName.toUpperCase().trim() + '%'));
            }

            if (!StringUtils.isEmpty(personnelNumber)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("personnelNumber")), personnelNumber.toUpperCase().trim()));
            }

            if (!StringUtils.isEmpty(position)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("position")), '%' + position.toUpperCase().trim() + '%'));
            }

            return predicates.size() > 1
                    ? criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]))
                    : predicates.get(0);
        };
    }
}
