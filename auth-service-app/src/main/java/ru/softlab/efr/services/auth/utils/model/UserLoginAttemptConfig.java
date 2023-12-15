package ru.softlab.efr.services.auth.utils.model;

/**
 * Java bean с параметрами конфигурации
 */
public class UserLoginAttemptConfig {
    private Integer wrongPasswordMaxAttemptCount;
    private Integer wrongPasswordTimeout;
    private Integer wrongPasswordPeriod;

    public UserLoginAttemptConfig() {
    }

    public UserLoginAttemptConfig(Integer wrongPasswordMaxAttemptCount, Integer wrongPasswordTimeout, Integer wrongPasswordPeriod) {
        this.wrongPasswordMaxAttemptCount = wrongPasswordMaxAttemptCount;
        this.wrongPasswordTimeout = wrongPasswordTimeout;
        this.wrongPasswordPeriod = wrongPasswordPeriod;
    }

    public Integer getWrongPasswordMaxAttemptCount() {
        return wrongPasswordMaxAttemptCount;
    }

    public void setWrongPasswordMaxAttemptCount(Integer wrongPasswordMaxAttemptCount) {
        this.wrongPasswordMaxAttemptCount = wrongPasswordMaxAttemptCount;
    }

    public Integer getWrongPasswordTimeout() {
        return wrongPasswordTimeout;
    }

    public void setWrongPasswordTimeout(Integer wrongPasswordTimeout) {
        this.wrongPasswordTimeout = wrongPasswordTimeout;
    }

    public Integer getWrongPasswordPeriod() {
        return wrongPasswordPeriod;
    }

    public void setWrongPasswordPeriod(Integer wrongPasswordPeriod) {
        this.wrongPasswordPeriod = wrongPasswordPeriod;
    }
}
