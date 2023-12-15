package ru.softlab.efr.services.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.services.auth.exchange.model.PasswordCharset;
import ru.softlab.efr.services.auth.exchange.model.PasswordCheckSettings;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.PasswordSettingsRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PasswordSettingsService {

    private final static String CHECK_ENABLED_KEY_NAME = "PASSWORD_SETTINGS_CHECK_ENABLED";
    private final static String MIN_LENGTH_KEY_NAME = "PASSWORD_SETTINGS_MIN_LENGTH";
    private final static String MAX_LENGTH_KEY_NAME = "PASSWORD_SETTINGS_MAX_LENGTH";
    private final static String NUMBER_OF_DIFFERENT_CHARACTERS_KEY_NAME = "PASSWORD_SETTINGS_NUMBER_OF_DIFFERENT_CHARACTERS";
    private final static String ENABLED_CHARSETS_KEY_NAME = "PASSWORD_SETTINGS_ENABLED_CHARSETS";
    private final static String REQUIRED_CHARSETS_KEY_NAME = "PASSWORD_SETTINGS_REQUIRED_CHARSETS";
    private final static String SPECIAL_CHARSETS_KEY_NAME = "PASSWORD_SETTINGS_SPECIAL_CHARSETS";

    @Autowired
    private PasswordSettingsRepository passwordSettingsRepository;

    @Transactional
    public void update(PasswordCheckSettings entity) {

        List<SettingEntity> entityList = passwordSettingsRepository.findAll();
        for (SettingEntity settingEntity : entityList) {
            switch (settingEntity.getKey()) {
                case CHECK_ENABLED_KEY_NAME:
                    settingEntity.setValue(String.valueOf(entity.isCheckEnabled()));
                    break;
                case MIN_LENGTH_KEY_NAME:
                    settingEntity.setValue(String.valueOf(entity.getMinLength()));
                    break;
                case MAX_LENGTH_KEY_NAME:
                    settingEntity.setValue(String.valueOf(entity.getMaxLength()));
                    break;
                case NUMBER_OF_DIFFERENT_CHARACTERS_KEY_NAME:
                    settingEntity.setValue(String.valueOf(entity.getNumberOfDifferentCharacters()));
                    break;
                case SPECIAL_CHARSETS_KEY_NAME:
                    settingEntity.setValue(entity.getSpecialCharsets());
                    break;
                case ENABLED_CHARSETS_KEY_NAME:
                    settingEntity.setValue(String.valueOf(entity.getEnabledCharsets()
                            .stream()
                            .map(PasswordCharset::name)
                            .collect(Collectors.joining(","))));
                    break;
                case REQUIRED_CHARSETS_KEY_NAME:
                    settingEntity.setValue(String.valueOf(entity.getRequiredCharsets()
                            .stream()
                            .map(PasswordCharset::name)
                            .collect(Collectors.joining(","))));
                    break;
            }
        }

        passwordSettingsRepository.save(entityList);
    }

    @Transactional(readOnly = true)
    public PasswordCheckSettings getCurrentSettings() {
        List<SettingEntity> entityList = passwordSettingsRepository.findAll();
        PasswordCheckSettings settings = new PasswordCheckSettings();
        for (SettingEntity entity : entityList) {
            switch (entity.getKey()) {
                case CHECK_ENABLED_KEY_NAME:
                    settings.setCheckEnabled(Boolean.valueOf(entity.getValue()));
                    break;
                case MIN_LENGTH_KEY_NAME:
                    settings.setMinLength(Integer.valueOf(entity.getValue()));
                    break;
                case MAX_LENGTH_KEY_NAME:
                    settings.setMaxLength(Integer.valueOf(entity.getValue()));
                    break;
                case NUMBER_OF_DIFFERENT_CHARACTERS_KEY_NAME:
                    settings.setNumberOfDifferentCharacters(Integer.valueOf(entity.getValue()));
                    break;
                case SPECIAL_CHARSETS_KEY_NAME:
                    settings.setSpecialCharsets(entity.getValue());
                    break;
                case ENABLED_CHARSETS_KEY_NAME:
                    if (entity.getValue().isEmpty()) {
                        settings.setEnabledCharsets(Collections.emptyList());
                        break;
                    }
                    List<PasswordCharset> listEnabled = Arrays.stream(entity.getValue().split(","))
                            .map(PasswordCharset::valueOf)
                            .distinct()
                            .sorted(Comparator.naturalOrder())
                            .collect(Collectors.toList());
                    settings.setEnabledCharsets(listEnabled);
                    break;
                case REQUIRED_CHARSETS_KEY_NAME:
                    if (entity.getValue().isEmpty()) {
                        settings.setRequiredCharsets(Collections.emptyList());
                        break;
                    }
                    List<PasswordCharset> listRequired = Arrays.stream(entity.getValue().split(","))
                            .map(PasswordCharset::valueOf)
                            .distinct()
                            .sorted(Comparator.naturalOrder())
                            .collect(Collectors.toList());
                    settings.setRequiredCharsets(listRequired);
                    break;
            }
        }

        return settings;
    }
}
