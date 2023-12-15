package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.softlab.efr.services.auth.model.Right;

/**
 * Репозиторий доступных прав для ролей
 *
 * @author akrenev
 * @since 15.02.2018
 */
public interface RightsRepository extends JpaRepository<Right, Long> {
}
