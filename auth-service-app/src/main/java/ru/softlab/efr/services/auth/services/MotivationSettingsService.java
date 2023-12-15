package ru.softlab.efr.services.auth.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.services.auth.exchange.model.MotivationSettings;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.MotivationSettingsRepository;

import java.util.List;

@Service
public class MotivationSettingsService {

    private static final Logger LOGGER = Logger.getLogger(MotivationSettingsService.class);

    private final static String CHECK_ENABLED_KEY = "MOTIVATION_SETTINGS_IS_ENABLED";
    private final static String CHECK_EXPIRATION_KEY = "MOTIVATION_SETTINGS_EXPIRE_TIME";

    private MotivationSettingsRepository motivationSettingsRepository;

    @Autowired
    public MotivationSettingsService(MotivationSettingsRepository motivationSettingsRepository) {
        this.motivationSettingsRepository = motivationSettingsRepository;
    }

    @Transactional
    public void update(MotivationSettings entity) {
        List<SettingEntity> entityList = motivationSettingsRepository.findAll();
        for (SettingEntity settingEntity : entityList) {
            switch (settingEntity.getKey()) {
                case CHECK_ENABLED_KEY:
                    settingEntity.setValue(String.valueOf(entity.isIsEnabled()));
                    break;
                case CHECK_EXPIRATION_KEY:
                    settingEntity.setValue(String.valueOf(entity.getExpireTime()));
            }
        }
        motivationSettingsRepository.save(entityList);
    }

    @Transactional(readOnly = true)
    public MotivationSettings getCurrentSettings() {
        MotivationSettings settings = new MotivationSettings();
        List<SettingEntity> entityList = motivationSettingsRepository.findAll();
        for (SettingEntity settingEntity : entityList) {
            switch (settingEntity.getKey()) {
                case CHECK_ENABLED_KEY:
                    settings.setIsEnabled(Boolean.valueOf(settingEntity.getValue()));
                    break;
                case CHECK_EXPIRATION_KEY:
                    settings.setExpireTime(Integer.valueOf(settingEntity.getValue()));
                    break;
            }
        }
        return settings;
    }
}
