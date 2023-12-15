package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.auth.model.Segment;

/**
 * Репозиторий для работы со справочником сегментов.
 */
public interface SegmentRepository extends JpaRepository<Segment, Long>{

  @Transactional (readOnly = true)
  Segment findSegmentByCode(String code);

  @Transactional (readOnly = true)
  Segment findSegmentByName(String name);
}
