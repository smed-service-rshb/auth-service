package ru.softlab.efr.test.services.auth.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.softlab.efr.test.infrastructure.transport.rest.MockRestRule;
import ru.softlab.efr.test.services.auth.rest.AuthorizedRestRule;

/**
 * Конфиг, поставляющий утилиту для интеграционного тестирования REST-сервисов с данными авторизации.
 *
 * @author akrenev
 * @since 27.02.2018
 */
@Configuration
public class AuthTestConfiguration {
    /**
     * Получить rest-клиента с поддержкой аутентификации
     *
     * @param mockRest оригинальный rest-клиент
     * @return rest-клиент с поддержкой аутентификации
     */
    @Bean
    public AuthorizedRestRule mockAuthRest(MockRestRule mockRest) {
        return new AuthorizedRestRule(mockRest);
    }
}
