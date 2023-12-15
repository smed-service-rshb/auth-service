package ru.softlab.efr.services.authorization.interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import ru.softlab.efr.infrastructure.transport.MicroServiceRequestInterceptor;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.PrincipalDataStore;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;

/**
 * Interceptor, вставляющий в каждый запрос, исходящий из сервиса информацию о текущем авторизованном пользователе
 * для поддерки проверки прав при транзитивных запросах
 *
 * @author niculichev
 * @since 19.05.2017
 */
@Component(ClientInterceptor.BEAN_NAME)
public class ClientInterceptor implements MicroServiceRequestInterceptor {

    public static final String BEAN_NAME = "microServiceRequestInterceptor";
    private static final Log log = LogFactory.getLog(ClientInterceptor.class);

    private final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    private PrincipalDataStore store;

    /**
     * ctor
     * @param store хранилище данных принципала (autowired, см. AuthorizationBeansRegistrar)
     */
    @Autowired
    public ClientInterceptor(PrincipalDataStore store){
        this.store = store;
    }

    @Override
    public void intercept(HttpRequest httpRequest, byte[] bytes) {
        PrincipalData principalData = store.getPrincipalData();
        if(principalData == null)
            return;

        try {
            httpRequest.getHeaders().add(PermissionControlConfig.HTTP_HEAD, serializer.serialize(principalData));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
