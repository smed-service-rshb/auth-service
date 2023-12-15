package ru.softlab.efr.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;
import ru.softlab.efr.infrastructure.transport.client.impl.JmsUriBuilder;
import ru.softlab.efr.services.auth.exceptions.*;
import ru.softlab.efr.services.auth.exchange.ChangePasswordRq;
import ru.softlab.efr.services.auth.exchange.CreateSessionRq;
import ru.softlab.efr.services.auth.exchange.CreateSessionRs;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static ru.softlab.efr.services.auth.AuthServiceInfo.SERVICE_NAME;

/**
 * Клиент управления сессиями
 *
 * @author niculichev
 * @since 27.04.2017
 */
@Service
public class SessionsManageAuthServiceClient extends AuthServiceClientBase {
    @Autowired
    public SessionsManageAuthServiceClient(MicroServiceTemplate microServiceTemplate) {
        this(microServiceTemplate, AuthServiceInfo.SERVICE_PREFIX);
    }

    public SessionsManageAuthServiceClient(MicroServiceTemplate microServiceTemplate, String prefix) {
        super(microServiceTemplate, prefix);
    }

    /**
     * Создание сессиии (аутентификация)
     * POST /session
     *
     * @param rq тело запроса
     * @return тело ответа
     */
    public CreateSessionRs postSession(CreateSessionRq rq, long timeout) throws Exception {
        return resultPostSession(futurePostSession(rq), timeout);
    }

    /**
     * Результат обработки "Создание сессиии (аутентификация)"
     *
     * @param future  результат асинхронной обработки
     * @param timeout время ожидания ответа
     * @return тело ответа
     * @throws Exception
     */
    public CreateSessionRs resultPostSession(ListenableFuture<ResponseEntity<CreateSessionRs>> future, long timeout) throws Exception {
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS).getBody();
        } catch (ExecutionException e) {
            switch (getStatusCode(e)) {
                case BAD_REQUEST:
                    throw new DataValidationException(getErrors(e));
                case UNAUTHORIZED:
                    throw new UserAuthenticationException();
                case BAD_GATEWAY:
                    throw new ConnectActiveDirectoryException();
                case GATEWAY_TIMEOUT:
                    throw new TimeoutActiveDirectoryException();
                case NOT_ACCEPTABLE:
                    getBadEntityError(e).raise();
                case INTERNAL_SERVER_ERROR:
                    throw new SystemException();
                default:
                    throw e;
            }
        }
    }

    /**
     * Создание сессиии (аутентификация)
     * POST /session
     *
     * @param rq тело запроса
     * @return результат асинхронной обработки
     */
    public ListenableFuture<ResponseEntity<CreateSessionRs>> futurePostSession(CreateSessionRq rq) throws Exception {
        Assert.notNull(rq, "rq was null");

        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/session")
                .build();

        RequestEntity<CreateSessionRq> requestEntity = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(rq);

        return microServiceTemplate.exchange(requestEntity, CreateSessionRs.class);
    }


    public void changePassword(ChangePasswordRq rq, long timeout) throws Exception {
        resultChangePassword(futureChangePasswordSession(rq), timeout);
    }

    private ListenableFuture<ResponseEntity<Void>> futureChangePasswordSession(ChangePasswordRq rq) throws Exception {
        Assert.notNull(rq, "rq was null");

        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/changePassword")
                .build();

        RequestEntity<ChangePasswordRq> requestEntity = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(rq);

        return microServiceTemplate.exchange(requestEntity, Void.class);
    }


    private void resultChangePassword(ListenableFuture<ResponseEntity<Void>> future, long timeout) throws Exception {
        try {
            future.get(timeout, TimeUnit.MILLISECONDS).getBody();
        } catch (ExecutionException e) {
            switch (getStatusCode(e)) {
                case BAD_REQUEST:
                    throw new DataValidationException(getErrors(e));
                case BAD_GATEWAY:
                    throw new ConnectActiveDirectoryException();
                case GATEWAY_TIMEOUT:
                    throw new TimeoutActiveDirectoryException();
                case NOT_ACCEPTABLE:
                    getBadEntityError(e).raise();
                case INTERNAL_SERVER_ERROR:
                    throw new SystemException();
                default:
                    throw e;
            }
        }
    }

    /**
     * Обновление сессии.
     *
     * @param id     Идентификатор сессии
     * @param office Наименование офиса
     * @param branch Наименование филиала
     */
    public void updateSession(String id, String office, String branch, long timeout) throws Exception {
        resultUpdateSession(futureUpdateSession(id, office, branch), timeout);
    }

    /**
     * Результат обработки "Обновление сессии".
     *
     * @param future  Результат асинхронной обработки.
     * @param timeout Время ожидания ответа.
     */
    @SuppressWarnings("Duplicates")
    public void resultUpdateSession(ListenableFuture<ResponseEntity<Void>> future, long timeout) throws Exception {
        try {
            future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            switch (getStatusCode(e)) {
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
     * Обновление сессии.
     *
     * @param id     Идентификатор сессии
     * @param office Наименование офиса
     * @param branch Наименование филиала
     */
    public ListenableFuture<ResponseEntity<Void>> futureUpdateSession(String id, String office, String branch) throws Exception {
        Assert.notNull(id, "id was null");
        Assert.notNull(office, "office was null");
        Assert.notNull(branch, "branch was null");

        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/session/{id}")
                .variable("id", id)
                .param("officeName", office)
                .param("branchName", branch)
                .build();

        RequestEntity<Void> requestEntity = RequestEntity.post(uri).build();
        return microServiceTemplate.exchange(requestEntity, Void.class);
    }

    /**
     * Закрытие сессии
     * DELETE /session/{id}
     *
     * @param id      идентификатор сессии
     * @param timeout таймаут для получения ответа
     * @throws Exception
     */
    public void deleteSession(String id, long timeout) throws Exception {
        resultDeleteSession(futureDeleteSession(id), timeout);
    }

    /**
     * Результат обработки "Закрытие сессии"
     *
     * @param future  результат асинхронной обработки
     * @param timeout время ожидания ответа
     * @throws Exception
     */
    public void resultDeleteSession(ListenableFuture<ResponseEntity<Void>> future, long timeout) throws
            Exception {
        try {
            future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            switch (getStatusCode(e)) {
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
     * Закрытие сессии
     * DELETE /session/{id}
     *
     * @param id идентификатор сессии
     */
    public ListenableFuture<ResponseEntity<Void>> futureDeleteSession(String id) throws Exception {
        Assert.notNull(id, "id was null");

        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/session/{id}")
                .variable("id", id)
                .build();

        RequestEntity<Void> requestEntity = RequestEntity.delete(uri).build();
        return microServiceTemplate.exchange(requestEntity, Void.class);
    }
}
