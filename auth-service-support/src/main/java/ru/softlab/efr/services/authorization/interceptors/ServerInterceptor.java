package ru.softlab.efr.services.authorization.interceptors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.PrincipalDataStore;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor, вызываемый непосредтсвенно перед вызовом веб-метода в контролере.
 * Занимается наполнением PrincipalDataStore
 *
 * @author niculichev
 * @since 19.05.2017
 */
@Component(ServerInterceptor.BEAN_NAME)
public class ServerInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = Logger.getLogger(ServerInterceptor.class);
    public static final String BEAN_NAME = "serverRequestInterceptor";

    private final PrincipalDataSerializer deserializer = new PrincipalDataSerializer();

    private final PrincipalDataStore store;

    /**
     * Конструктор
     *
     * @param store хранилище данных принципала (autowired, см. AuthorizationBeansRegistrar)
     */
    public ServerInterceptor(PrincipalDataStore store) {
        this.store = store;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) {
        if (!(o instanceof HandlerMethod)) {
            return true;
        }
        try {
            String authorizationHeaderValue = httpServletRequest.getHeader(PermissionControlConfig.HTTP_HEAD);
            if (StringUtils.isNotEmpty(authorizationHeaderValue)) {
                store.setPrincipalData(deserializer.deserialize(authorizationHeaderValue));
            }
        } catch (Exception e) {
            LOGGER.error("Не удалось распарсить информацию по авторизации.", e);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
        //Перехватчик должен выполняться только перед выполнением контроллера
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        store.clearPrincipalData();
    }
}
