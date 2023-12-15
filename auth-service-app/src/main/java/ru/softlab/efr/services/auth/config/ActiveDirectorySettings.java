package ru.softlab.efr.services.auth.config;

/**
 * Настройки подключения к AD
 *
 * @author akrenev
 * @since 16.03.2018
 */
public class ActiveDirectorySettings {
    private String mainURL;
    private String backupURL;

    private int connectTimeout;
    private String contextSearch;

    private String serviceUserDN;
    private String serviceUserPsw;

    /**
     * Конструктор
     *
     * @param mainURL        URL основного подключения к LDAP
     * @param backupURL      URL резервного подключения к LDAP
     * @param connectTimeout таймаут соединения с AD
     * @param contextSearch  базовый DN для поиска
     * @param serviceUserDN  DN сервисного пользователя
     * @param serviceUserPsw пароль сервисного пользователя
     */
    ActiveDirectorySettings(String mainURL, String backupURL, int connectTimeout, String contextSearch, String serviceUserDN, String serviceUserPsw) {
        this.mainURL = mainURL;
        this.backupURL = backupURL;
        this.connectTimeout = connectTimeout;
        this.contextSearch = contextSearch;
        this.serviceUserDN = serviceUserDN;
        this.serviceUserPsw = serviceUserPsw;
    }

    /**
     * Получить URL основного подключения к LDAP
     *
     * @return URL
     */
    public String getMainURL() {
        return mainURL;
    }

    /**
     * Получить URL резервного подключения к LDAP
     *
     * @return URL
     */

    public String getBackupURL() {
        return backupURL;
    }

    /**
     * Получить базовый DN для поиска
     *
     * @return базовый DN для поиска
     */
    public String getContextSearch() {
        return contextSearch;
    }

    /**
     * Получить таймаут соединения с AD
     *
     * @return значение таймаута в милисекундах
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Получить DN сервисного пользователя
     *
     * @return DN сервисного пользователя
     */
    public String getServiceUserDN() {
        return serviceUserDN;
    }

    /**
     * Получить пароль сервисного пользователя
     *
     * @return пароль сервисного пользователя
     */
    public String getServiceUserPsw() {
        return serviceUserPsw;
    }
}
