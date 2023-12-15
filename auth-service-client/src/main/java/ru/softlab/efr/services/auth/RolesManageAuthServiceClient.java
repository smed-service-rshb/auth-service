package ru.softlab.efr.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;
import ru.softlab.efr.infrastructure.transport.client.impl.JmsUriBuilder;
import ru.softlab.efr.services.auth.exceptions.*;
import ru.softlab.efr.services.auth.exchange.*;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static ru.softlab.efr.services.auth.AuthServiceInfo.SERVICE_NAME;

/**
 * Клиент управления ролями
 *
 * @author niculichev
 * @since 27.04.2017
 */
@SuppressWarnings("Duplicates")
@Service
public class RolesManageAuthServiceClient extends AuthServiceClientBase{
    @Autowired
    public RolesManageAuthServiceClient(MicroServiceTemplate microServiceTemplate){
        this(microServiceTemplate, AuthServiceInfo.SERVICE_PREFIX);
    }

    public RolesManageAuthServiceClient(MicroServiceTemplate microServiceTemplate, String prefix){
        super(microServiceTemplate, prefix);
    }

    /**
     * Список ролей и разрешений
     * GET /roles?details={details}
     * @param timeout время ожидания ответа
     * @return тело ответа
     */
    public GetRolesRs getRoles(long timeout) throws Exception {
        return resultGetRoles(futureGetRoles(), timeout);
    }

    /**
     * Результат обработки "Список ролей и разрешений"
     * @param future результат асинхронной обработки
     * @param timeout время ожидания ответа
     * @return тело ответа
     * @throws Exception
     */
    public GetRolesRs resultGetRoles(ListenableFuture<ResponseEntity<GetRolesRs>> future, long timeout) throws Exception {
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS).getBody();
        } catch (ExecutionException e){
            switch (getStatusCode(e)){
                case FORBIDDEN:
                    throw new PermissionDeniedException();
                case INTERNAL_SERVER_ERROR:
                    throw new SystemException();
                default:
                    throw e;
            }
        }
    }

    /**
     * Список ролей и разрешений
     * GET /roles?details={details}
     * @return результат асинхронной обработки
     */
    public ListenableFuture<ResponseEntity<GetRolesRs>> futureGetRoles() throws Exception{
        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/roles")
                .build();

        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        return microServiceTemplate.exchange(requestEntity, GetRolesRs.class);
    }

    /**
     * Получение всех допустимых прав
     * GET /rights
     * @param timeout время ожидания ответа
     * @return тело ответа
     * @throws Exception ошибка выполнения запроса
     */
    public GetAllRightDataRs getAllRights(long timeout) throws Exception {
        return resultGetRights(futureGetAllRights(), timeout);
    }


    /**
     * Результат обработки "Получение прав"
     * @param future результат асинхронной обработки
     * @param timeout время ожидания ответа
     * @return тело ответа
     * @throws Exception ошибка выполнения запроса
     */
    public <T> T resultGetRights(ListenableFuture<ResponseEntity<T>> future, long timeout) throws Exception {
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS).getBody();
        } catch (ExecutionException e){
            switch (getStatusCode(e)){
                case FORBIDDEN:
                    throw new PermissionDeniedException();
                case INTERNAL_SERVER_ERROR:
                    throw new SystemException();
                default:
                    throw e;
            }
        }
    }

    /**
     * Получение данных всех допустимых прав
     * GET /rights
     *
     * @return результат асинхронной обработки
     * @throws Exception ошибка выполнения запроса
     */
    public ListenableFuture<ResponseEntity<GetAllRightDataRs>> futureGetAllRights() throws Exception {
        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/rights")
                .build();

        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        return microServiceTemplate.exchange(requestEntity, GetAllRightDataRs.class);
    }

    /**
     * Получение данных роли
     * GET /roles/{id}
     * @param id - внутренний идентификатор
     * @param timeout время ожидания ответа
     * @return тело ответа
     */
    public GetRoleRs getRole(Long id, long timeout) throws Exception {
        return resultGetRole(futureGetRole(id), timeout);
    }

    /**
     * Результат обработки "Получение данных роли"
     * @param future результат асинхронной обработки
     * @param timeout время ожидания ответа
     * @throws Exception
     */
    public GetRoleRs resultGetRole(ListenableFuture<ResponseEntity<GetRoleRs>> future, long timeout) throws Exception {
        try{
            return future.get(timeout, TimeUnit.MILLISECONDS).getBody();
        } catch (ExecutionException e){
            switch (getStatusCode(e)){
                case NOT_FOUND:
                    throw new EntityNotFoundException();
                case FORBIDDEN:
                    throw new PermissionDeniedException();
                case INTERNAL_SERVER_ERROR:
                    throw new SystemException();
                default:
                    throw e;
            }
        }
    }

    /**
     * Получение данных роли
     * GET /roles/{id}
     * @param id - внутренний идентификатор
     * @return результат асинхронной обработки
     */
    public ListenableFuture<ResponseEntity<GetRoleRs>> futureGetRole(Long id) throws Exception{
        Assert.notNull(id, "id was null");
        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/roles/{id}")
                .variable("id", id)
                .build();

        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        return microServiceTemplate.exchange(requestEntity, GetRoleRs.class);
    }

    /**
     * Создание роли
     * POST /roles
     * @param timeout время ожидания ответа
     * @param roleRq тело запроса
     */
    public EntryIdRs postRoles(SetRoleRq roleRq, long timeout) throws Exception {
        return resultPostRoles(futurePostRoles(roleRq), timeout);
    }

    /**
     * Результат обработки "Создание роли"
     * @param future результат асинхронной обработки
     * @param timeout время ожидания ответа
     * @throws Exception
     */
    public EntryIdRs resultPostRoles(ListenableFuture<ResponseEntity<EntryIdRs>> future, long timeout) throws Exception {
        try{
            return future.get(timeout, TimeUnit.MILLISECONDS).getBody();
        } catch (ExecutionException e){
            switch (getStatusCode(e)){

                case FORBIDDEN:
                    throw new PermissionDeniedException();
                case BAD_REQUEST:
                    throw new DataValidationException(getErrors(e));
                case CONFLICT:
                    throw new ConstraintEntityParametersException();
                case INTERNAL_SERVER_ERROR:
                    throw new SystemException();
                default:
                    throw e;
            }
        }
    }

    /**
     * Создание роли
     * POST /roles
     * @param role тело запроса
     * @return результат асинхронной обработки
     */
    public ListenableFuture<ResponseEntity<EntryIdRs>> futurePostRoles(SetRoleRq role) throws Exception{
        Assert.notNull(role, "role was null");
        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/roles")
                .build();

        RequestEntity<SetRoleRq> requestEntity = RequestEntity.post(uri).body(role);
        return microServiceTemplate.exchange(requestEntity, EntryIdRs.class);
    }


    /**
     * Редактирование роли
     * PUT /roles
     * @param roleRq тело запроса
     * @param timeout время ожидания ответа
     */
    public void putRoles(SetRoleRq roleRq, long timeout) throws Exception {
        resultPutRoles(futurePutRoles(roleRq), timeout);
    }

    /**
     * Результат обработки "Редактирование роли"
     * @param future результат асинхронной обработки
     * @param timeout время ожидания ответа
     * @throws Exception
     */
    public void resultPutRoles(ListenableFuture<ResponseEntity<Void>> future, long timeout) throws Exception {
        try{
            future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e){
            switch (getStatusCode(e)){

                case FORBIDDEN:
                    throw new PermissionDeniedException();
                case BAD_REQUEST:
                    throw new DataValidationException(getErrors(e));
                case CONFLICT:
                    throw new ConstraintEntityParametersException();
                case INTERNAL_SERVER_ERROR:
                    throw new SystemException();
                default:
                    throw e;
            }
        }
    }

    /**
     * Редактирование роли
     * PUT /roles
     * @param role тело запроса
     * @return результат асинхронной обработки
     */
    public ListenableFuture<ResponseEntity<Void>> futurePutRoles(SetRoleRq role) throws Exception{
        Assert.notNull(role, "role was null");
        Assert.notNull(role.getId(), "role.id was null");

        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/roles/{id}")
                .variable("id", role.getId())
                .build();

        RequestEntity<SetRoleRq> requestEntity = RequestEntity.put(uri).body(role);
        return microServiceTemplate.exchange(requestEntity, Void.class);
    }

    /**
     * Удаление роли
     * DELETE /roles/{id}
     * @param id идентификатор роли
     * @param timeout время ожидания ответа
     */
    public void deleteRole(Long id, long timeout) throws Exception {
        resultDeleteRole(futureDeleteRole(id), timeout);
    }

    /**
     * Результат обработки "Удаление роли"
     * @param future результат асинхронной обработки
     * @param timeout время ожидания ответа
     * @throws Exception
     */
    public void resultDeleteRole(ListenableFuture<ResponseEntity<Void>> future, long timeout) throws Exception {
        try{
            future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e){
            switch (getStatusCode(e)){

                case FORBIDDEN:
                    throw new PermissionDeniedException();
                case NOT_FOUND:
                    throw new EntityNotFoundException();
                case CONFLICT:
                    throw new ConstraintEntityParametersException();
                case INTERNAL_SERVER_ERROR:
                    throw new SystemException();
                default:
                    throw e;
            }
        }
    }

    /**
     * Удаление роли
     * DELETE /roles/{id}
     * @param id идентификатор роли
     * @return результат асинхронной обработки
     */
    public ListenableFuture<ResponseEntity<Void>> futureDeleteRole(Long id) throws Exception{
        Assert.notNull(id, "id was null");

        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/roles/{id}")
                .variable("id", id)
                .build();

        RequestEntity<Void> requestEntity = RequestEntity.delete(uri).build();
        return microServiceTemplate.exchange(requestEntity, Void.class);
    }
}
