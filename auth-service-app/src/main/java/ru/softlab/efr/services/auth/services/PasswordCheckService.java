package ru.softlab.efr.services.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.services.auth.exceptions.BadQualityPasswordException;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.PasswordSettingsRepository;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Сервис, предоставляющий возможности проверки и настройки качества паролей пользователя.
 *
 * @author Andrey Grigorov
 */
@Service
public class PasswordCheckService {

    private final static String CHECK_ENABLED_KEY_NAME = "PASSWORD_SETTINGS_CHECK_ENABLED";
    private final static String MIN_LENGTH_KEY_NAME = "PASSWORD_SETTINGS_MIN_LENGTH";
    private final static String MAX_LENGTH_KEY_NAME = "PASSWORD_SETTINGS_MAX_LENGTH";
    private final static String NUMBER_OF_DIFFERENT_CHARACTERS_KEY_NAME = "PASSWORD_SETTINGS_NUMBER_OF_DIFFERENT_CHARACTERS";
    private final static String ENABLED_CHARSETS_KEY_NAME = "PASSWORD_SETTINGS_ENABLED_CHARSETS";
    private final static String REQUIRED_CHARSETS_KEY_NAME = "PASSWORD_SETTINGS_REQUIRED_CHARSETS";
    private final static String SPECIAL_CHARSETS_KEY_NAME = "PASSWORD_SETTINGS_SPECIAL_CHARSETS";

    @Autowired
    private PasswordSettingsRepository passwordSettingsRepository;


    public void checkPassword(String password) throws BadQualityPasswordException {

        boolean isEnabled = false;
        int minLength = 0;
        int maxLength = 0;
        int numberOfDiff = 0;
        String specialChars = "";
        List<PasswordCharset> enabledCharsets = new ArrayList<>();
        List<PasswordCharset> requiredCharsets = new ArrayList<>();

        List<SettingEntity> entityList = passwordSettingsRepository.findAll();
        for (SettingEntity entity : entityList) {
            switch (entity.getKey()) {
                case CHECK_ENABLED_KEY_NAME:
                    isEnabled = Boolean.parseBoolean(entity.getValue());
                    break;
                case MIN_LENGTH_KEY_NAME:
                    minLength = Integer.valueOf(entity.getValue());
                    break;
                case MAX_LENGTH_KEY_NAME:
                    maxLength = Integer.valueOf(entity.getValue());
                    break;
                case NUMBER_OF_DIFFERENT_CHARACTERS_KEY_NAME:
                    numberOfDiff = Integer.valueOf(entity.getValue());
                    break;
                case SPECIAL_CHARSETS_KEY_NAME:
                    specialChars = entity.getValue();
                    break;
                case ENABLED_CHARSETS_KEY_NAME:
                    enabledCharsets = getEnabledCharsets(entity.getValue());
                    break;
                case REQUIRED_CHARSETS_KEY_NAME:
                    requiredCharsets = getRequiredCharsets(entity.getValue());
                    break;
            }
        }

        if (isEnabled) {
            List<String> errors = new ArrayList<>();

            if (password.length() < minLength) {
                errors.add("Пароль должен иметь длину не менее " + minLength + " символов");
            }

            if (password.length() > maxLength) {
                errors.add("Пароль должен иметь длину не более " + maxLength + " символов");
            }

            long diffCharsCount = password.chars().distinct().count();
            if (diffCharsCount < numberOfDiff) {
                errors.add("Пароль должен содержать не менее " + numberOfDiff + " различных символов");
            }

            // проверка того, что пароль состоит только из допустимых символов
            for (char ch : password.toCharArray()) {
                if (!charContainsInCharsets(ch, enabledCharsets, specialChars)) {
                    String charsetsTitle = enabledCharsets.stream()
                            .map(PasswordCharset::getTitle)
                            .collect(Collectors.joining(", "));
                    errors.add("Пароль должен содержать символы только из следующих наборов: " + charsetsTitle);
                    break;
                }
            }

            // проверка того, что пароль содержит символы из всех обязательных множеств
            Set<PasswordCharset> unusedRequiredCharsets = new HashSet<>(requiredCharsets);

            for (char ch : password.toCharArray()) {
                PasswordCharset charset = detectCharset(ch, specialChars);
                unusedRequiredCharsets.remove(charset);
            }

            if (!unusedRequiredCharsets.isEmpty()) {
                String charsetsTitle = requiredCharsets.stream()
                        .map(PasswordCharset::getTitle)
                        .collect(Collectors.joining(", "));
                errors.add("Пароль обязательно должен содержать хотя бы по одному символу из следующих наборов: " + charsetsTitle);
            }

            if (!errors.isEmpty()) {
                throw new BadQualityPasswordException(errors);
            }
        }
    }

    private PasswordCharset detectCharset(char ch, String specialChars) {
        for (PasswordCharset charset : PasswordCharset.values()) {
            Pattern pattern = charset.getPattern();
            if ((pattern != null) && (pattern.matcher(String.valueOf(ch)).matches())) {
                return charset;
            }
        }

        if (specialChars.contains(String.valueOf(ch))) {
            return PasswordCharset.SPECIAL;
        }

        return null;
    }

    private boolean charContainsInCharsets(char ch, List<PasswordCharset> charsets, String specialChars) {
        for (PasswordCharset charset : charsets) {
            Pattern pattern = charset.getPattern();
            if ((pattern != null) && (pattern.matcher(String.valueOf(ch)).matches())) {
                return true;
            }
        }

        if (charsets.contains(PasswordCharset.SPECIAL)) {
            return specialChars.contains(String.valueOf(ch));
        }

        return false;
    }

    private List<PasswordCharset> getEnabledCharsets(String enabledCharsets) {
        return Arrays.stream(enabledCharsets.split(","))
                .map(PasswordCharset::valueOf)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    private List<PasswordCharset> getRequiredCharsets(String requiredCharsets) {
        return Arrays.stream(requiredCharsets.split(","))
                .map(PasswordCharset::valueOf)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }
}
