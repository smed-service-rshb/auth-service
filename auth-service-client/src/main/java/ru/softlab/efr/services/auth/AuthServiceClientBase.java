package ru.softlab.efr.services.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;
import ru.softlab.efr.services.auth.exchange.BadEntityRs;
import ru.softlab.efr.services.auth.exchange.ErrorsDataRs;

/**
 * Базовый класс классов-клиентов для работы с auth-service
 *
 * @author niculichev
 * @since 29.04.2017
 */
public class AuthServiceClientBase {
    protected final String prefix;
    protected final MicroServiceTemplate microServiceTemplate;

    protected AuthServiceClientBase(MicroServiceTemplate microServiceTemplate, String prefix){
        this.prefix = prefix;
        this.microServiceTemplate = microServiceTemplate;
    }

    /**
     * Извлеч код статуса из исключения, если возможно. Иначе прокидывается передаваемое исключение
     * @param e исключение с возможной инфомацией о статусе
     * @return http статус
     * @throws Exception передаваемое исключение
     */
    protected HttpStatus getStatusCode(Exception e) throws Exception {
        if(e.getCause() instanceof HttpStatusCodeException){
            return ((HttpStatusCodeException) e.getCause()).getStatusCode();
        }

        throw e;
    }

    /**
     * Извлеч тело ответа из исключения, если возможно. Иначе прокидывается передаваемое исключение
     * @param e исключение с возможным телом ответа
     * @return тело ответа
     * @throws Exception передаваемое исключение
     */
    protected String getBody(Exception e) throws Exception {
        if(e.getCause() instanceof HttpStatusCodeException){
            return ((HttpStatusCodeException) e.getCause()).getResponseBodyAsString();
        }

        throw e;
    }

    /**
     * Извлечь ошибки валидации из исключения, если возможно. Иначе прокидывается передаваемое исключение
     * @param e исключение с возможным телом ответа
     * @return сущность ошибок валидации
     * @throws Exception передаваемое исключение
     */
    protected ErrorsDataRs getErrors(Exception e) throws Exception{
        return new ObjectMapper().readValue(getBody(e), ErrorsDataRs.class);
    }

    protected BadEntityRs getBadEntityError(Exception e) throws Exception{
        return new ObjectMapper().readValue(getBody(e), BadEntityRs.class);
    }
}
