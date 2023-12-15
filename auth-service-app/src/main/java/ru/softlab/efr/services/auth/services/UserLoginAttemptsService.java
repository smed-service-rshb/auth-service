package ru.softlab.efr.services.auth.services;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.common.settings.entities.SettingEntity;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.UserLoginAttempt;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.LoginAttemptSettingsRepository;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserLoginAttemptsRepository;
import ru.softlab.efr.services.auth.utils.model.UserLoginAttemptConfig;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервси контроля количеситва попыток ввода неверного пароля
 */
@Service
public class UserLoginAttemptsService {

    private static final String WRONG_PASSWORD_MAX_ATTEMPT_COUNT = "WRONG_PASSWORD_MAX_ATTEMPT_COUNT";
    private static final int WRONG_PASSWORD_MAX_ATTEMPT_COUNT_DEFAULT_VALUE = 10;

    private static final String WRONG_PASSWORD_TIMEOUT = "WRONG_PASSWORD_TIMEOUT";
    private static final Integer WRONG_PASSWORD_TIMEOUT_DEFAULT_VALUE = 120;

    private static final String WRONG_PASSWORD_PERIOD = "WRONG_PASSWORD_PERIOD";
    private static final Integer WRONG_PASSWORD_PERIOD_DEFAULT_VALUE = 300;

    private UserLoginAttemptsRepository userLoginAttemptsRepository;

    private LoginAttemptSettingsRepository settingsRepository;

    @Autowired
    public void setUserLoginAttemptsRepository(UserLoginAttemptsRepository userLoginAttemptsRepository) {
        this.userLoginAttemptsRepository = userLoginAttemptsRepository;
    }

    @Autowired
    public void setSettingsRepository(LoginAttemptSettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    /**
     * Сохранить информацию по неудачной попытке входа
     */
    @Transactional
    public UserLoginAttempt saveLoginAttempts(Employee user) {
        UserLoginAttempt userLoginAttempt = new UserLoginAttempt();
        userLoginAttempt.setCreationDate(LocalDateTime.now());
        userLoginAttempt.setUser(user);
        return userLoginAttemptsRepository.save(userLoginAttempt);
    }

    /**
     * Удалить информацию о всех неудачных попытках входа пользователя в систему
     */
    @Transactional
    public void deleteAllAttemptsByUser(Employee user) {
        userLoginAttemptsRepository.deleteAllByUser(user);
    }

    public boolean isMaxAttemptExceeded(Employee user) {
        UserLoginAttemptConfig cfg = getConfigParams();
        return isMaxAttemptExceeded(user, cfg.getWrongPasswordMaxAttemptCount(), cfg.getWrongPasswordPeriod());
    }

    public boolean isMaxAttemptExceeded(Employee user, Integer wrongPasswordMaxAttemptCount, Integer wrongPasswordPeriod) {
        //Если максимальное кол-во попыток указано "-1" значит ограничений по кол-ву попыток ввода пароля нет
        if (wrongPasswordMaxAttemptCount.equals(-1)) {
            return false;
        }
        Integer attemptsNumber = 0;
        if (wrongPasswordPeriod.equals(-1)) {  //нет ограничения по периоду
            attemptsNumber = userLoginAttemptsRepository.countAllByUser(user);
        } else {
            attemptsNumber = userLoginAttemptsRepository.countAllByUserAndCreationDateAfter(user, LocalDateTime.now().minusSeconds(wrongPasswordPeriod));
        }
        return attemptsNumber.compareTo(wrongPasswordMaxAttemptCount) > 0;
    }

    /**
     * Получить параметры конфигурации политики безопасности в части количества допустимых попыток ввода неправильного пароля
     */
    public UserLoginAttemptConfig getConfigParams() {
        UserLoginAttemptConfig config = new UserLoginAttemptConfig();
        List<SettingEntity> entityList = settingsRepository.findAll();
        for (SettingEntity entity : entityList) {
            switch (entity.getKey()) {
                case WRONG_PASSWORD_MAX_ATTEMPT_COUNT:
                    config.setWrongPasswordMaxAttemptCount(NumberUtils.createInteger(entity.getValue()));
                    break;
                case WRONG_PASSWORD_TIMEOUT:
                    config.setWrongPasswordTimeout(NumberUtils.createInteger(entity.getValue()));
                    break;
                case WRONG_PASSWORD_PERIOD:
                    config.setWrongPasswordPeriod(NumberUtils.createInteger(entity.getValue()));
                    break;
                default:
                    break;
            }
        }
        if (config.getWrongPasswordMaxAttemptCount() == null) {
            config.setWrongPasswordMaxAttemptCount(WRONG_PASSWORD_MAX_ATTEMPT_COUNT_DEFAULT_VALUE);
        }
        if (config.getWrongPasswordTimeout() == null) {
            config.setWrongPasswordTimeout(WRONG_PASSWORD_TIMEOUT_DEFAULT_VALUE);
        }
        if (config.getWrongPasswordPeriod() == null) {
            config.setWrongPasswordPeriod(WRONG_PASSWORD_PERIOD_DEFAULT_VALUE);
        }
        return config;
    }
}
