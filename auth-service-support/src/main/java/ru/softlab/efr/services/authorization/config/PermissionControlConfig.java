package ru.softlab.efr.services.authorization.config;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import ru.softlab.efr.services.authorization.*;
import ru.softlab.efr.services.authorization.interceptors.ServerInterceptor;
import ru.softlab.efr.services.authorization.services.PermissionStoreService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Конфигурация для настройки проверки прав и регитсрации бинов, содержащий информацию по правам
 *
 * @author niculichev
 * @since 19.05.2017
 */
@Configuration
@ComponentScan(basePackages = "ru.softlab.efr.services.authorization")
@EnableCaching
public class PermissionControlConfig extends WebMvcConfigurerAdapter {
    public static final String HTTP_HEAD = "EFR-Authorization";

    public static final String PERMISSION_CACHE_MANAGER = "AuthServiceSupportPermissions";
    public static final String PERMISSION_TO_RIGHT_CACHE_NAME = "PermissionsToRightsMapping";

    private static final int CACHE_TTL_SECONDS = 600;

    @Autowired
    private ApplicationContext appContext;

    /**
     * Получить источник данных принципала
     *
     * @return источник данных принципала
     */
    @Bean
    public PrincipalDataSource principalDataStore() {
        return new ThreadLocalPrincipalDataStore();
    }

    /**
     * Получить фабрику для контекста безопасности
     *
     * @param permissionStoreService Серсис для работы разрешениями
     * @return Фабрика для контекста безопасности
     */
    @Bean
    public SecurityContextFactory securityContextFactory(PermissionStoreService permissionStoreService) {
        return new SecurityContextFactory(permissionStoreService);
    }

    /**
     * Получить контекста безопасности
     *
     * @param securityContextFactory Серсис для работы разрешениями
     * @param principalDataSource Источник данных принципала
     * @return контекста безопасности
     */
    @Bean
    public SecurityContext securityContext(SecurityContextFactory securityContextFactory, PrincipalDataSource principalDataSource) {
        return securityContextFactory.createContext(principalDataSource);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor((HandlerInterceptor) appContext.getBean(ServerInterceptor.BEAN_NAME));
    }

    /**
     * Получить менеджер кешей для разрешений
     *
     * @return менеджер кешей для разрешений
     */
    @Bean(PERMISSION_CACHE_MANAGER)
    public CacheManager authServiceSupportPermissionsCacheManager() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration(PERMISSION_TO_RIGHT_CACHE_NAME, 0)
                .eternal(false)
                .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
                .timeToLiveSeconds(CACHE_TTL_SECONDS)
                .timeToIdleSeconds(CACHE_TTL_SECONDS);
        net.sf.ehcache.CacheManager manager = net.sf.ehcache.CacheManager.create();
        manager.addCache(new Cache(cacheConfiguration));
        return new EhCacheCacheManager(manager);
    }

    @Bean
    HandlerExceptionResolver customExceptionResolver() {
        return new AbstractHandlerExceptionResolver() {
            @Override
            public int getOrder() {
                return HIGHEST_PRECEDENCE;
            }

            @Override
            protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                if (!(ex instanceof ForbiddenException)) {
                    return null;
                }
                try {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                } catch (IOException e) {
                    logger.error("Ошибка установки статуса FORBIDDEN", e);
                }
                return new ModelAndView();
            }
        };
    }
}
