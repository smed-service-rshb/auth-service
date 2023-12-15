package ru.softlab.efr.services.auth;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import ru.softlab.efr.common.utilities.rest.EntityExistsException;
import ru.softlab.efr.common.utilities.rest.NotFoundException;
import ru.softlab.efr.common.utilities.rest.client.ClientRestResult;
import ru.softlab.efr.common.utilities.rest.client.RestClientException;
import ru.softlab.efr.common.utilities.rest.client.RestUtils;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceTemplate;
import ru.softlab.efr.infrastructure.transport.client.MicroServiceURIBuilder;
import ru.softlab.efr.infrastructure.transport.client.impl.JmsUriBuilder;
import ru.softlab.efr.services.auth.exceptions.DataValidationException;
import ru.softlab.efr.services.auth.exceptions.EntityNotFoundException;
import ru.softlab.efr.services.auth.exceptions.PermissionDeniedException;
import ru.softlab.efr.services.auth.exceptions.SystemException;
import ru.softlab.efr.services.auth.exchange.GetEmployeesRs;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static ru.softlab.efr.services.auth.AuthServiceInfo.SERVICE_NAME;

/**
 * Клиент управления сотрдуниками
 *
 * @author niculichev
 * @since 27.04.2017
 */
@SuppressWarnings("Duplicates")
@Service
public class EmployeesManageAuthServiceClient extends AuthServiceClientBase {
    private static final String REQUIRED_PARAM_ERROR = "Не указан обязательный параметр '%s' при вызове %s";
    private static final String STATUS_CODE_TEMPLATE = "status code: [%s]";
    private static final String OFFICE_PARAMETER_NAME = "office";
    private static final String PERSONNEL_NUMBER_PARAMETER_NAME = "personnelNumber";
    private static final String GET_EMPLOYEE_METHOD_NAME = "getEmployee";

    /**
     * Возвращяет экземпляр класса
     * @param microServiceTemplate - объект, реализующий интерфейс {@link MicroServiceTemplate}
     */
    @Autowired
    public EmployeesManageAuthServiceClient(MicroServiceTemplate microServiceTemplate) {
        this(microServiceTemplate, AuthServiceInfo.SERVICE_PREFIX);
    }

    /**
     * Возвращяет экземпляр класса
     * @param microServiceTemplate - объект, реализующий интерфейс {@link MicroServiceTemplate}
     * @param prefix - префикс урла
     */
    public EmployeesManageAuthServiceClient(MicroServiceTemplate microServiceTemplate, String prefix) {
        super(microServiceTemplate, prefix);
    }

    /**
     * Список сотрудников
     * GET /employees
     *
     * @param office  офис сотрудника
     * @param timeout время ожидания ответа
     * @return тело ответа
     * @throws Exception ошибка обработки запроса
     */
    public GetEmployeesRs getEmployees(String office, long timeout) throws Exception {
        return resultGetEmployees(futureGetEmployees(office), timeout);
    }

    /**
     * Результат обработки получени сотрудников
     * GET /employees
     *
     * @param future  результат асинхронной обработки
     * @param timeout время ожидания ответа
     * @return результат асинхронной обработки
     * @throws Exception ошибка обработки запроса
     */
    public GetEmployeesRs resultGetEmployees(ListenableFuture<ResponseEntity<GetEmployeesRs>> future, long timeout) throws Exception {
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
     * Список сотрудников
     * GET /employees
     *
     * @param office офис сотрудника
     * @return результат асинхронной обработки
     * @throws Exception ошибка обработки запроса
     */
    public ListenableFuture<ResponseEntity<GetEmployeesRs>> futureGetEmployees(String office) throws Exception {
        if (StringUtils.isEmpty(office)) {
            throw new IllegalArgumentException("Ожидается параметр 'office'");
        }
        URI uri = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/employees")
                .param(OFFICE_PARAMETER_NAME, office)
                .build();

        RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
        return microServiceTemplate.exchange(requestEntity, GetEmployeesRs.class);
    }

    /**
     * Получить сотрудника
     * GET /employee-rights
     *
     * @param personnelNumber персональный номер (required)
     * @param office          офис сотрудника (required)
     * @param timeout         таймаут ожидания ответа на асинхронный запрос
     * @return найденный сотрудник
     * @throws RestClientException в случае неудачи
     */
    public UserData getEmployee(String personnelNumber, String office, long timeout) throws RestClientException {
        return getEmployee(personnelNumber, office).get(timeout);
    }

    /**
     * Получить сотрудника
     * GET /employee-rights
     *
     * @param personnelNumber персональный номер (required)
     * @param office          офис сотрудника (required)
     * @return найденный сотрудник
     * @throws RestClientException в случае неудачи
     */
    public ClientRestResult<UserData> getEmployee(String personnelNumber, String office) throws RestClientException {
        return new ClientRestResult<>(getEmployeeInternal(personnelNumber, office), this::processStatusCodeErrors);
    }

    private ListenableFuture<ResponseEntity<UserData>> getEmployeeInternal(String personnelNumber, String office) throws RestClientException {
        if (personnelNumber == null) {
            throw new RestClientException(String.format(REQUIRED_PARAM_ERROR, PERSONNEL_NUMBER_PARAMETER_NAME, GET_EMPLOYEE_METHOD_NAME));
        }
        if (office == null) {
            throw new RestClientException(String.format(REQUIRED_PARAM_ERROR, OFFICE_PARAMETER_NAME, GET_EMPLOYEE_METHOD_NAME));
        }

        MicroServiceURIBuilder uriBuilder = JmsUriBuilder.service(SERVICE_NAME)
                .path(prefix + "/employee")
                .param(PERSONNEL_NUMBER_PARAMETER_NAME, personnelNumber)
                .param(OFFICE_PARAMETER_NAME, office);

        RequestEntity requestEntity = RequestEntity
                .method(HttpMethod.GET, uriBuilder.build())
                .build();
        return microServiceTemplate.exchange(requestEntity, UserData.class);
    }

    private void processStatusCodeErrors(Exception e) throws RestClientException {
        HttpStatus statusCode = RestUtils.getStatusCode(e);
        if (statusCode != null) {
            switch (statusCode) {
                case INTERNAL_SERVER_ERROR:
                    throw new RestClientException(String.format(STATUS_CODE_TEMPLATE, HttpStatus.INTERNAL_SERVER_ERROR.toString()));
                case CONFLICT:
                    throw new EntityExistsException();
                case NOT_FOUND:
                    throw new NotFoundException();
                default:
                    throw new RestClientException(e.toString());
            }
        }
        throw new RestClientException(e.toString());
    }
}
