package ru.softlab.efr.services.auth.services.impl.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.softlab.efr.common.settings.entities.SettingEntity;

import java.util.List;


public interface MotivationSettingsRepository extends JpaRepository<SettingEntity, Long> {

    @Query("select s from SettingEntity s where s.key like 'MOTIVATION_SETTINGS_%'")
    @Override
    List<SettingEntity> findAll();
}
