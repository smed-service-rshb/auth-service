package ru.softlab.efr.services.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Конфигурация для взаимодействия с active directory. Сами настройки находятся в файле active-directory.properties
 *
 * @author niculichev
 * @since 27.04.2017
 */
@Configuration
@PropertySource("classpath:active-directory.properties")
public class ActiveDirectoryConfig {
    @Value("${active.directory.connect.main-url.GO}")
    private String goMainURL;
    @Value("${active.directory.connect.backup-url.GO}")
    private String goBackupURL;

    @Value("${active.directory.connect.timeout.GO:0}")
    private int goConnectTimeout;
    @Value("${active.directory.context.search.GO}")
    private String goContextSearch;

    @Value("${active.directory.service.user-dn.GO}")
    private String goServiceUserDN;
    @Value("${active.directory.service.password.GO}")
    private String goServiceUserPsw;

    @Value("${active.directory.connect.main-url.RF}")
    private String rfMainURL;
    @Value("${active.directory.connect.backup-url.RF}")
    private String rfBackupURL;

    @Value("${active.directory.connect.timeout.RF:0}")
    private int rfConnectTimeout;
    @Value("${active.directory.context.search.RF}")
    private String rfContextSearch;

    @Value("${active.directory.service.user-dn.RF}")
    private String rfServiceUserDN;
    @Value("${active.directory.service.password.RF}")
    private String rfServiceUserPsw;

    /**
     * Получить настройки подключения к AD GO
     *
     * @return настройки подключения к AD
     */
    public ActiveDirectorySettings getGOSettings() {
        return new ActiveDirectorySettings(goMainURL, goBackupURL, goConnectTimeout, goContextSearch, goServiceUserDN, goServiceUserPsw);
    }

    /**
     * Получить настройки подключения к AD RF
     *
     * @return настройки подключения к AD
     */
    public ActiveDirectorySettings getRFSettings() {
        return new ActiveDirectorySettings(rfMainURL, rfBackupURL, rfConnectTimeout, rfContextSearch, rfServiceUserDN, rfServiceUserPsw);
    }

}
