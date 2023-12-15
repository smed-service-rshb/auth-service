package ru.softlab.efr.services.auth.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.MotivationDocumentSettingsRepository;

import java.util.List;

@Service
public class MotivationDocumentSettingsService {

    private static final Logger LOGGER = Logger.getLogger(MotivationDocumentSettingsService.class);

    private final static String ID = "MOTIVATION_DOCUMENT_SETTINGS_ID";
    private final static String NAME = "MOTIVATION_DOCUMENT_SETTINGS_NAME";

    private MotivationDocumentSettingsRepository motivationDocumentSettingsRepository;

    @Autowired
    public MotivationDocumentSettingsService(MotivationDocumentSettingsRepository motivationDocumentSettingsRepository) {
        this.motivationDocumentSettingsRepository = motivationDocumentSettingsRepository;
    }

    @Transactional(readOnly = true)
    public String getIdSettings() {
        List<SettingEntity> entityList = motivationDocumentSettingsRepository.findAll();
        for (SettingEntity settingEntity : entityList) {
            if (ID.equals(settingEntity.getKey())) {
                return settingEntity.getValue();
            }
        }
        LOGGER.info("fileId not set in config service");
        return null;
    }

    @Transactional(readOnly = true)
    public String getFilenameSettings() {
        List<SettingEntity> entityList = motivationDocumentSettingsRepository.findAll();
        for (SettingEntity settingEntity : entityList) {
            if (NAME.equals(settingEntity.getKey())) {
                return settingEntity.getValue();
            }
        }
        LOGGER.info("file name not set in config service");
        return null;
    }
}
