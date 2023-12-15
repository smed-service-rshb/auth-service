package ru.softlab.efr.services.authorization.annotations;

import org.springframework.context.annotation.Import;
import ru.softlab.efr.common.utilities.hibernate.annotations.AddHibernateJpaPackages;
import ru.softlab.efr.services.authorization.config.AuthorizationBeansRegistrar;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация включающая возможность проверки прав. Данную аннотацию требуется подключить к основной конфигурации.
 * Таким образом регистрируются системные endpoint'ы для установки соответствия глобальных ролей и локальных для
 * сервиса разрешений.
 *
 * @author niculichev
 * @since 19.05.2017
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AddHibernateJpaPackages(
        repositoryPackages = "ru.softlab.efr.services.authorization.repositories",
        modelPackages = "ru.softlab.efr.services.authorization.model")
@Import({AuthorizationBeansRegistrar.class, PermissionControlConfig.class})
public @interface EnablePermissionControl {
    /**
     * @return имя бина, реализующего хранения данных приципала (ru.softlab.efr.services.authorization.PrincipalDataStore)
     */
    String source() default "principalDataStore";

    /**
     * @return имя бина для обработки событий изменения состояния сессии(закрытия сессии, например)
     */
    String sessionListenerRef() default "sessionListener";
}
