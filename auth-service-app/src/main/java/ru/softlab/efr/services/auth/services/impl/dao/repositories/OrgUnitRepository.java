package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.auth.model.OrgUnit;
import ru.softlab.efr.services.auth.model.OrgUnitType;

import java.util.List;

/**
 * Репозиторий для работы с сущностью организационной штатной структуры.
 */
public interface OrgUnitRepository extends JpaRepository<OrgUnit, Long>{

  @Transactional (readOnly = true)
  OrgUnit findTopByIdIn(List<Long> ids);

  @Transactional (readOnly = true)
  List<OrgUnit> findOrgUnitsByParent(Long parent);

  @Transactional (readOnly = true)
  Page<OrgUnit> findOrgUnitsByParent(Long parent, Pageable pageable);

  @Transactional (readOnly = true)
  Page<OrgUnit> findOrgUnitsByCity(String city, Pageable pageable);

  @Transactional (readOnly = true)
  Page<OrgUnit> findOrgUnitsByType(OrgUnitType type, Pageable pageable);

  @Transactional (readOnly = true)
  List<OrgUnit> findOrgUnitsByType(OrgUnitType type);

  @Transactional (readOnly = true)
  List<OrgUnit> findOrgUnitsByNameContainsAndType(String name, OrgUnitType type);

  @Transactional (readOnly = true)
  List<OrgUnit> findOrgUnitsByNameAndType(String name, OrgUnitType type);
}
