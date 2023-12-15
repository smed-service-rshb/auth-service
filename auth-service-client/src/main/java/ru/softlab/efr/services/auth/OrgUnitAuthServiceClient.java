package ru.softlab.efr.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;
import ru.softlab.efr.infrastructure.transport.client.impl.JmsUriBuilder;
import ru.softlab.efr.services.auth.exceptions.DataValidationException;
import ru.softlab.efr.services.auth.exceptions.EntityNotFoundException;
import ru.softlab.efr.services.auth.exceptions.PermissionDeniedException;
import ru.softlab.efr.services.auth.exceptions.SystemException;
import ru.softlab.efr.services.auth.exchange.GetOrgUnitFullRs;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static ru.softlab.efr.services.auth.AuthServiceInfo.SERVICE_NAME;

/**
 * Клиент для работы с орг.структурой организации.
 *
 * @author Andrey Grigorov
 */
@SuppressWarnings("Duplicates")
@Service
public class OrgUnitAuthServiceClient extends AuthServiceClientBase {

    /**
     * Возвращяет экземпляр класса
     *
     * @param microServiceTemplate - объект, реализующий интерфейс {@link MicroServiceTemplate}
     */
    @Autowired
    public OrgUnitAuthServiceClient(MicroServiceTemplate microServiceTemplate) {
        this(microServiceTemplate, AuthServiceInfo.SERVICE_PREFIX);
    }

    /**
     * Возвращяет экземпляр класса
     *
     * @param microServiceTemplate - объект, реализующий интерфейс {@link MicroServiceTemplate}
     * @param prefix               - префикс урла
     */
    public OrgUnitAuthServiceClient(MicroServiceTemplate microServiceTemplate, String prefix) {
        super(microServiceTemplate, prefix);
    }

    /**
     * Получение списка орг.структур
     * GET /orgunits
     *
     * @param timeout время ожидания ответа в милисекундах
     * @return тело ответа
     * @throws Exception ошибка обработки запроса
     */
    public GetOrgUnitFullRs getOrgUnitList(long timeout) throws Exception {
        return resultGetOrgUnitList(futureGetOrgUnitList(), timeout);
    }

    /**
     * Результат обработки получения списка орг.структур
     * GET /orgunits
     *
     * @param future  результат асинхронной обработки
     * @param timeout время ожидания ответа
     * @return результат асинхронной обработки
     * @throws Exception ошибка обработки запроса
     */
    private GetOrgUnitFullRs resultGetOrgUnitList(ListenableFuture<ResponseEntity<GetOrgUnitFullRs>> future, long timeout) throws Exception {
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS).getBody();
        } catch (ExecutionException e) {
            switch (getStatusCode(e)) {
                case FORBIDDEN:
                    throw new PermissionDeniedException();
                case BAD_REQUEST:
                    throw new DataValidationException(getErrors(e));
                case NOT_FOUND:
                    throw new EntityNotFoundException();
                case INTERNAL_SERVER_ERROR:
                    throw new SystemException();
                default:
                    throw e;
            }
        }
    }

    /**
     * Получение списка орг.структур
     * GET /orgunits
     *
     * @return результат асинхронной обработки
     * @throws Exception ошибка обработки запроса
     */
    private ListenableFuture<ResponseEntity<GetOrgUnitFullRs>> futureGetOrgUnitList() {

        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/orgunits")
                .build();

        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        return microServiceTemplate.exchange(requestEntity, GetOrgUnitFullRs.class);
    }
}
