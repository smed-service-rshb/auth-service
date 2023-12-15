package ru.softlab.efr.services.auth.controllers;

import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.model.OperationMode;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.efr.infrastructure.logging.api.services.OperationLogService;
import ru.softlab.efr.services.auth.ErrorData;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.config.Permissions;
import ru.softlab.efr.services.auth.exceptions.*;
import ru.softlab.efr.services.auth.exchange.ErrorsDataRs;
import ru.softlab.efr.services.auth.exchange.model.*;
import ru.softlab.efr.services.auth.model.*;
import ru.softlab.efr.services.auth.services.*;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserRepository;
import ru.softlab.efr.services.auth.utils.DateHelper;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataSource;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.authorization.annotations.HasRight;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.auth.controllers.Utils.buildErrorRes;

/**
 * Контроллер обработки запросов, связанных с сотрудниками
 *
 * @author niculichev
 * @since 12.04.2017
 */
@Validated
@RestController
@PropertySource("classpath:ValidationMessages.properties")
public class EmployeeController implements EmployeesApi {
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class);
    private static final String OFFICE_FIELD_NAME = "office";
    private static final String LOGIN_FIELD_NAME = "login";
    private static final String PASSWORD_FIELD_NAME = "password";
    private static final String SEGMENT_ID_FIELD_NAME = "segment";
    private static final String USER_ID_FIELD_NAME = "user";
    private static final String ERROR_LOG_KEY = "error";

    @Value("${UserData.office.InvalidFormat}")
    private String invalidOfficeFormat;

    @Value("${UserData.office.NotFound}")
    private String officeNotFound;

    @Value("${Segment.NotFound}")
    private String segmentNotFound;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private OrgUnitService orgUnitService;

    @Autowired
    private SegmentService segmentService;

    @Autowired
    private AuthenticateService authenticateService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private PasswordCheckService passwordCheckService;

    @Autowired
    @Qualifier("roleServiceDaoImpl")
    private RoleStoreService roleService;

    @Autowired
    private UserLockService userLockService;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private EmployeeGroupService employeeGroupService;

    @Autowired
    private UserLoginAttemptsService userLoginAttemptsService;

    @Autowired
    private PrincipalDataSource principalDataSource;

    /**
     * Получение списка учётных записей всех сотрудников.
     * Список отсортирован по ФИО сотрудников.
     *
     * @return Список учётных записей сотрудников.
     */
    @HasPermission(Permissions.GET_EMPLOYEES)
    public ResponseEntity<Page<EmployeeDataForList>> getEmployees(@PageableDefault(value = 50) Pageable pageable,
                                                                  @Valid @RequestBody FilterEmployeesRq filterData,
                                                                  @Valid @RequestParam(value = "hasFilter", required = false) Boolean hasFilter) {
        Page<Employee> employees;
        if (Boolean.TRUE.equals(hasFilter) && filterData != null) {
            if (Objects.isNull(filterData.getMotivationCorrectStatus())) {
                employees = employeeService.findAllWithFilter(false, pageable,
                        filterData.getSecondName(), filterData.getFirstName(), filterData.getMiddleName(),
                        filterData.getPersonnelNumber(), filterData.getOrgUnitIdes(), filterData.getBranches(),
                        filterData.getPosition(), filterData.getRoleId());
            } else {
                employees = employeeService.findAllWithFilterAndWithMotivationCheck(false, pageable,
                        filterData.getSecondName(), filterData.getFirstName(), filterData.getMiddleName(),
                        filterData.getPersonnelNumber(), filterData.getOrgUnitIdes(), filterData.getBranches(),
                        filterData.getPosition(), filterData.getRoleId(), filterData.getMotivationCorrectStatus());
            }
        } else {
            employees = employeeService.findAll(false, pageable);
        }
        Page<EmployeeDataForList> response = employees.map(employee -> {
            Boolean lock = userLockService.isLocked(employee.getId());
            return convertUserListInfo(employee, lock);
        });
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<List<EmployeeDataForList>> getEmployeesWithOutPermissions(@Valid @RequestBody FilterEmployeesRq filterData) throws Exception {
        List<Employee> employees = employeeService.findAllByFilter(filterData.getSecondName(), filterData.getFirstName(), DateHelper.toDate(filterData.getBirthDate()), filterData.getMobilePhone());
        List<EmployeeDataForList> response = employees.stream().map(employee -> {
            Boolean lock = userLockService.isLocked(employee.getId());
            return convertUserListInfo(employee, lock);
        }).collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<List<EmployeeDataForList>> getEmployeesWithOutPermissionsWithDeleted() throws Exception {
        List<Employee> employees = employeeService.findAllWithDeleted();
        List<EmployeeDataForList> response = employees.stream()
                .map(EmployeeController::convertUserToReportInfo).collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<UpdateListStatus> statusSyncEmployeeList() throws Exception {
        return ResponseEntity.ok(new UpdateListStatus(employeeService.getUpdateStatus()));
    }

    @Override
    public ResponseEntity<UpdateListStatus> syncEmployeeList() throws Exception {
        return ResponseEntity.ok(new UpdateListStatus(employeeService.updateList()));
    }

    /**
     * Поиск информации о сотруднике по идентификатору.
     *
     * @param id идентификатор сотрудника
     * @return сотрудник
     */
    @HasPermission(Permissions.GET_EMPLOYEE)
    public ResponseEntity<GetEmployeeRs> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.findByIdAndNotDeleted(id);

        if (employee != null) {
            List<Right> employeeRights = authenticateService.getRights(employee);
            Boolean lock = userLockService.isLocked(employee.getId());
            return ResponseEntity.ok().body(buildEmployeeInfo(employee, employeeRights, lock));
        } else {
            LOGGER.error(String.format("Сотрудник не найден по идентификатору %s.", id));
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<EmploeeDataWithOrgUnits> getEmployeeByIdWithOutPermission(@PathVariable("id") Long id) throws Exception {
        Employee employee = employeeService.findByIdAndNotDeleted(id);

        if (employee != null) {
            return ResponseEntity.ok().body(buildEmployeeInfo(employee));
        } else {
            LOGGER.error(String.format("Сотрудник не найден по идентификатору %s.", id));
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<EmployeeDataForList> getEmployeeByLoginWithOutPermissions(String login) throws Exception {
        Employee employee = employeeService.findByLogin(login);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(convertUserListInfo(employee, userLockService.isLocked(employee.getId())));
    }

    private static GetEmployeeRs buildEmployeeInfo(Employee employee, List<Right> employeeRights, Boolean lock) {
        GetEmployeeRs response = new GetEmployeeRs();
        response.setId(employee.getId());
        response.setLogin(employee.getLogin());
        response.setFirstName(employee.getFirstName());
        response.setSecondName(employee.getSecondName());
        response.setMiddleName(employee.getMiddleName());
        response.setFirstName(employee.getFirstName());
        response.setEmail(employee.getEmail());
        response.setMobilePhone(employee.getMobilePhone());
        response.setInnerPhone(employee.getInnerPhone());
        response.setPosition(employee.getPosition());
        response.setPersonnelNumber(employee.getPersonnelNumber());
        response.setChangePassword(employee.isChangePassword());
        response.setLockStatus(lock);
        if (employee.getGroups() != null) {
            response.setGroupIds(employee.getGroups().stream().map(EmployeeGroup::getId).collect(Collectors.toList()));
        }
        if (employee.getOrgUnits() != null) {
            response.setOrgUnits(employee.getOrgUnits().stream().map(OrgUnit::getId).collect(Collectors.toList()));
        }

        if (employee.getSegment() != null) {
            response.setSegmentId(employee.getSegment().getId());
        }

        if (employee.getRoles() != null) {
            response.setRoles(employee.getRoles().stream().map(Role::getId).collect(Collectors.toList()));
        } else {
            response.setRoles(new ArrayList<>());
        }
        return response;
    }

    private static EmploeeDataWithOrgUnits buildEmployeeInfo(Employee employee) {
        EmploeeDataWithOrgUnits response = new EmploeeDataWithOrgUnits();
        response.setId(employee.getId());
        response.setLogin(employee.getLogin());
        response.setFirstName(employee.getFirstName());
        response.setSecondName(employee.getSecondName());
        response.setMiddleName(employee.getMiddleName());
        response.setFirstName(employee.getFirstName());
        response.setEmail(employee.getEmail());
        response.setMobilePhone(employee.getMobilePhone());
        response.setInnerPhone(employee.getInnerPhone());
        response.setPosition(employee.getPosition());
        response.setPersonnelNumber(employee.getPersonnelNumber());
        if (employee.getBirthDate() != null) {
            response.setBirthDate(employee.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
        if (employee.getGroups() != null) {
            response.setGroupIds(employee.getGroups().stream().map(EmployeeGroup::getId).collect(Collectors.toList()));
        }
        if (employee.getOrgUnits() != null) {
            response.setOrgUnits(employee.getOrgUnits().stream().map(EmployeeController::convert).collect(Collectors.toList()));
        }

        if (employee.getSegment() != null) {
            response.setSegmentId(employee.getSegment().getId());
        }
        return response;
    }

    private static OrgUnitData convert(OrgUnit orgUnit) {
        OrgUnitData orgUnitData = new OrgUnitData();
        orgUnitData.setId(orgUnit.getId());
        orgUnitData.setOffice(orgUnit.getName());
        orgUnitData.setBranch(orgUnit.getParent().getName());
        return orgUnitData;
    }

    @Override
    @HasRight(Right.CLIENT_VIEW_CONTRACTS_LIST)
    public ResponseEntity<Void> changeLogin(@Valid @RequestBody ChangeLoginData body) throws LoginAlreadyExistException {
        PrincipalData principalData = principalDataSource.getPrincipalData();

        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("LOGIN_CHANGE");
        operationLogEntry.setOperationDescription("Смена логина пользователем");
        operationLogEntry.setOperationParameter("firstName", principalData.getFirstName());
        operationLogEntry.setOperationParameter("secondName", principalData.getSecondName());
        operationLogEntry.setOperationParameter("middleName", principalData.getMiddleName());
        operationLogEntry.setOperationParameter("newLogin", body.getLogin());
        operationLogEntry.setOperationParameter("changeLoginTime", LocalDateTime.now().toString());
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);

        try {
            authenticateService.changeLogin(body.getLogin(), principalData);
            operationLogEntry.setOperationState(OperationState.SUCCESS);
        } catch (LoginAlreadyExistException ex) {
            String errorMessage = String.format("Ошибка при изменении логина пользователем, логин %s занят.", body.getLogin());
            operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
            operationLogEntry.setOperationParameter(ERROR_LOG_KEY, errorMessage);
            LOGGER.error(errorMessage, ex);
            throw ex;
        } catch (EntityNotFoundException ex) {
            String errorMessage = "Ошибка при изменении логина пользователем, пользователь не найден.";
            LOGGER.error(errorMessage, ex);
            operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
            operationLogEntry.setOperationParameter(ERROR_LOG_KEY, errorMessage);
            return ResponseEntity.notFound().build();
        } catch (UserIdentificationException ex) {
            String errorMessage = "Ошибка при изменении логина пользователем, найдено несколько клиентов.";
            LOGGER.error(errorMessage, ex);
            operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
            operationLogEntry.setOperationParameter(ERROR_LOG_KEY, errorMessage);
            return ResponseEntity.badRequest().build();
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> changeLoginWithOutPermission(@Valid @RequestBody PrivateChangeLoginData body) throws Exception {
        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("LOGIN_CHANGE");
        operationLogEntry.setOperationDescription("Смена логина пользователем");
        operationLogEntry.setOperationParameter("login", body.getOldLogin());
        operationLogEntry.setOperationParameter("newLogin", body.getNewLogin());
        operationLogEntry.setOperationParameter("changeLoginTime", LocalDateTime.now().toString());
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);

        try {
            authenticateService.changeLogin(body.getOldLogin(), body.getNewLogin());
            operationLogEntry.setOperationState(OperationState.SUCCESS);
        } catch (LoginAlreadyExistException ex) {
            String errorMessage = String.format("Ошибка при изменении логина пользователем, логин %s занят.", body.getNewLogin());
            operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
            operationLogEntry.setOperationParameter(ERROR_LOG_KEY, errorMessage);
            LOGGER.error(errorMessage, ex);
            throw ex;
        } catch (EntityNotFoundException ex) {
            String errorMessage = "Ошибка при изменении логина пользователем, пользователь не найден.";
            LOGGER.error(errorMessage, ex);
            operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
            operationLogEntry.setOperationParameter(ERROR_LOG_KEY, errorMessage);
            return ResponseEntity.notFound().build();
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordData changePasswordRequest) throws Exception {
        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("PASSWORD_CHANGE");
        operationLogEntry.setOperationDescription("Смена пароля пользователем");
        operationLogEntry.setOperationParameter("login", changePasswordRequest.getLogin());
        operationLogEntry.setOperationParameter("changePasswordTime", LocalDateTime.now().toString());
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);

        try {
            Employee authenticate = authenticateService.auth(changePasswordRequest.getLogin(), changePasswordRequest.getOldPassword(), null);
            if (authenticate == null) {
                throw new BadQualityPasswordException(Collections.singletonList("Вы ввели неправильный старый пароль"));
            }

            passwordCheckService.checkPassword(changePasswordRequest.getNewPassword());
            String passwordHash = passwordService.getPasswordHash(changePasswordRequest.getNewPassword());
            authenticate.setPasswordHash(passwordHash);
            authenticate.setChangePassword(false);

            userRepository.save(authenticate);

            operationLogEntry.setOperationState(OperationState.SUCCESS);

            return ResponseEntity.ok().build();
        }  catch (BadQualityPasswordException ex) {
            operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
            operationLogEntry.setOperationParameter("error", ex.getErrors());
            throw ex;
        } catch (UserIdentificationException | UserAuthenticationException e) {
            String errorMessage = "Ошибка аутентификации пользователя " + changePasswordRequest.getLogin();
            LOGGER.error(errorMessage, e);
            operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
            operationLogEntry.setOperationParameter("error", errorMessage);

            throw new BadQualityPasswordException(Collections.singletonList("Вы ввели неправильный старый пароль"));
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);
        }
    }

    @Override
    public ResponseEntity<ChangePasswordRs> changePasswordPrivate(@Valid @RequestBody ChangePasswordData changePasswordRequest) throws Exception {
        ChangePasswordRs rs = new ChangePasswordRs();
        try {
            changePassword(changePasswordRequest);
        } catch (BadQualityPasswordException ex) {
            List<ErrorDataErrors> errors = ex.getErrors().stream()
                    .map(message -> new ErrorDataErrors(PASSWORD_FIELD_NAME, message))
                    .collect(Collectors.toList());
            rs.setErrors(errors);
        }
        return ResponseEntity.ok(rs);
    }

    /**
     * Запрос на создание сотрудника
     *
     * @param createEmployeeRq данные сотрудника
     * @return в случае успеха id сотрудника
     */
    @Override
    @HasPermission(Permissions.CREATE_EMPLOYEE)
    public ResponseEntity<CreateEmployeeRs> createEmployee(@ApiParam(value = "данные сотрудника", required = true) @Valid @RequestBody CreateOrUpdateEmployeeRq createEmployeeRq) throws UserOfficeNotFoundException, SegmentNotFoundException, LoginAlreadyExistException, PasswordMissingException, BadQualityPasswordException, EmployeeGroupNotFoundException {
        Employee employee = new Employee();
        fillEmployeePropsFormRequest(createEmployeeRq, employee);
        if (((createEmployeeRq.getPassword() != null) && !createEmployeeRq.getPassword().isEmpty())) {
            passwordCheckService.checkPassword(createEmployeeRq.getPassword());
            String passwordHash = passwordService.getPasswordHash(createEmployeeRq.getPassword());
            employee.setPasswordHash(passwordHash);
        }
        employee = employeeService.create(employee);
        CreateEmployeeRs response = new CreateEmployeeRs(employee.getId());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CreateEmployeeRs> createEmployeeWithOutPermissions(@ApiParam(value = "данные сотрудника", required = true) @Valid @RequestBody CreateOrUpdateEmployeeRq createEmployeeRq) throws Exception {
        Employee employee = new Employee();
        fillEmployeePropsFormRequest(createEmployeeRq, employee);
        if (((createEmployeeRq.getPassword() != null) && !createEmployeeRq.getPassword().isEmpty())) {
            passwordCheckService.checkPassword(createEmployeeRq.getPassword());
            String passwordHash = passwordService.getPasswordHash(createEmployeeRq.getPassword());
            employee.setPasswordHash(passwordHash);
        }
        employee = employeeService.createBySelfRegistration(employee);
        CreateEmployeeRs response = new CreateEmployeeRs(employee.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * Обновить данные сотрудника
     *
     * @param id               идентификатор сотрудника
     * @param updateEmployeeRq данные сотрудника
     * @return ok-в случае успеха
     */
    @Override
    @HasPermission(Permissions.UPDATE_EMPLOYEE)
    public ResponseEntity<Void> updateEmployee(@PathVariable("id") Long id, @Valid @RequestBody CreateOrUpdateEmployeeRq updateEmployeeRq) throws Exception {
        return updateEmployeeInner(id, updateEmployeeRq);
    }

    @Override
    public ResponseEntity<Void> updateEmployeeWithOutPermissions(@PathVariable("id") Long id, @Valid @RequestBody CreateOrUpdateEmployeeRq updateEmployeeRq) throws Exception {
        return updateEmployeeInner(id, updateEmployeeRq);
    }

    @Override
    public ResponseEntity<Void> updateUserWithOutPermissions(@PathVariable("id") Long id, @Valid @RequestBody UpdateUserRq updateUserRq) throws Exception {
        Employee employee = employeeService.findByIdAndNotDeleted(id);
        if (employee == null) {
            String errorMessage = "Сотрудник не найден по идентификатору " + id;
            LOGGER.warn(errorMessage);
            return ResponseEntity.notFound().build();
        }
        employee.setFirstName(updateUserRq.getFirstName());
        employee.setMiddleName(updateUserRq.getMiddleName());
        employee.setSecondName(updateUserRq.getSecondName());
        employee.setBirthDate(Date.from(updateUserRq.getBirthDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        employee.setMobilePhone(updateUserRq.getMobilePhone());
        employee.setEmail(updateUserRq.getEmail());
        employeeService.update(employee);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Void> updateEmployeeInner(Long id, CreateOrUpdateEmployeeRq updateEmployeeRq) throws UserOfficeNotFoundException, SegmentNotFoundException, LoginAlreadyExistException, BadQualityPasswordException, EmployeeGroupNotFoundException {
        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("UPDATE_EMPLOYEE");
        operationLogEntry.setOperationDescription("Обновление учётной записи сотрудника");
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);

        try {
            Employee employee = employeeService.findByIdAndNotDeleted(id);
            if (employee == null) {
                String errorMessage = "Сотрудник не найден по идентификатору " + id;
                LOGGER.warn(errorMessage);
                operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
                operationLogEntry.setOperationParameter("error", errorMessage);
                return ResponseEntity.notFound().build();
            }

            operationLogEntry.setOperationParameter("oldEmployeeData", employee.toString());
            fillEmployeePropsFormRequest(updateEmployeeRq, employee);

            if (((updateEmployeeRq.getPassword() != null) && !updateEmployeeRq.getPassword().isEmpty())) {
                passwordCheckService.checkPassword(updateEmployeeRq.getPassword());
                String passwordHash = passwordService.getPasswordHash(updateEmployeeRq.getPassword());
                employee.setPasswordHash(passwordHash);
            }

            operationLogEntry.setOperationParameter("newEmployeeData", employee.toString());
            employeeService.update(employee);
            operationLogEntry.setOperationState(OperationState.SUCCESS);
            return ResponseEntity.ok().build();
        } catch (LoginAlreadyExistException ex) {
            operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
            operationLogEntry.setOperationParameter("error", "Пользователь с логином " + updateEmployeeRq.getLogin()
                    + " уже существует");
            throw ex;
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);
        }
    }

    /**
     * Удалить сотрудника
     *
     * @param id идентификатор сотрудника
     * @return ok-в случае успеха
     */
    @HasPermission(Permissions.DELETE_EMPLOYEE)
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.logicalDelete(id);
        } catch (javax.persistence.EntityNotFoundException e) {
            LOGGER.error(String.format("Ошибка обработки запроса: не удается найти сотрудника по идентификатору %s", id));
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Заблокировать сотрудника
     *
     * @param id             Идентификатор сотрудника
     * @param lockEmployeeRq Тело запроса блокировки пользователя
     * @return Ok
     */
    @Override
    @HasPermission(Permissions.LOCK_EMPLOYEE)
    public ResponseEntity<Void> lockEmployee(@PathVariable("id") Long id, @RequestBody(required = false) LockEmployeeRq lockEmployeeRq) throws Exception {
        userLockService.lock(id, lockEmployeeRq != null ? lockEmployeeRq.getDescription() : null, LockType.BY_HAND);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> resetClientPasswordWithOutPermission(@PathVariable("id") Long id,
                                                    @NotNull @Valid @RequestParam(value = "password") String password) {
        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("CHANGE_USER_PASSWORD");
        operationLogEntry.setOperationDescription("Установка технического пароля для клиента");
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);
        try {
            employeeService.resetClientPassword(id, password);
            operationLogEntry.setOperationState(OperationState.SUCCESS);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException ex){
            String errorMessage = String.format("Не удалось найти клиента с id: %s", id);
            LOGGER.error(errorMessage, ex);
            operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
            operationLogEntry.setOperationParameter(ERROR_LOG_KEY, errorMessage);
            return ResponseEntity.notFound().build();
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);
        }
    }

    @Override
    @HasPermission(Permissions.RESET_PASSWORD)
    public ResponseEntity<Void> resetPassword(@PathVariable Long id) throws Exception {
        try {
            employeeService.resetPassword(id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            LOGGER.error(String.format("Произошла ошибка при попытке сбросить пароль пользователя с id: %s.", id), ex);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @Override
    public ResponseEntity<Void> setPasswordWithOutPermission(@PathVariable("userId") Long userId, @Valid @RequestBody SetPasswordData setPasswordRq) throws Exception {
        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("CHANGE_USER_PASSWORD");
        operationLogEntry.setOperationDescription("Обновление пароля пользователя");
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);
        try {
            Employee user = employeeService.findByIdAndNotDeleted(userId);
            if (user == null) {
                String errorMessage = "Пользователь не найден по идентификатору " + userId;
                LOGGER.warn(errorMessage);
                operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
                operationLogEntry.setOperationParameter("error", errorMessage);
                return ResponseEntity.notFound().build();
            }
            passwordCheckService.checkPassword(setPasswordRq.getNewPassword());
            String passwordHash = passwordService.getPasswordHash(setPasswordRq.getNewPassword());
            user.setPasswordHash(passwordHash);
            user.setChangePassword(false);
            employeeService.update(user);
            operationLogEntry.setOperationState(OperationState.SUCCESS);
            return ResponseEntity.ok().build();
        } catch (BadQualityPasswordException ex) {
            operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
            operationLogEntry.setOperationParameter("error", "Ошибка валидации нового пароля");
            throw ex;
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);
        }
    }

    /**
     * Разблокировать сотрудника
     *
     * @param id Идентификатор сотрудника
     * @return Ok
     */
    @Override
    @HasPermission(Permissions.UNLOCK_EMPLOYEE)
    public ResponseEntity<Void> unlockEmployee(@PathVariable("id") Long id) throws Exception {
        userLockService.unlock(id);
        //При разблокировке удаляем информацию о неудачных попытках входа
        Employee employee = employeeService.findByIdAndNotDeleted(id);
        userLoginAttemptsService.deleteAllAttemptsByUser(employee);
        return ResponseEntity.ok().build();
    }

    private void fillEmployeePropsFormRequest(CreateOrUpdateEmployeeRq employeeData, Employee employee) throws UserOfficeNotFoundException, SegmentNotFoundException, EmployeeGroupNotFoundException {
        employee.setLogin(employeeData.getPersonnelNumber());
        employee.setFirstName(employeeData.getFirstName());
        employee.setSecondName(employeeData.getSecondName());
        employee.setMiddleName(employeeData.getMiddleName());
        employee.setInnerPhone(employeeData.getInnerPhone());
        employee.setMobilePhone(employeeData.getMobilePhone());
        employee.setEmail(employeeData.getEmail());
        employee.setBirthDate(DateHelper.toDate(employeeData.getBirthDate()));
        employee.setPosition(employeeData.getPosition());
        Set<OrgUnit> orgUnits = new HashSet<>();
        if (employeeData.getOrgUnits() != null) {
            for (Long orgUnitId : employeeData.getOrgUnits()) {
                OrgUnit orgUnit = orgUnitService.findById(orgUnitId);
                if (orgUnit == null) {
                    throw new UserOfficeNotFoundException(String.format("Ошибка обработки запроса: не удается найти объект оргструктуры по идентификатору %s", orgUnitId));
                }
                orgUnits.add(orgUnit);
            }
        }
        employee.setOrgUnits(orgUnits);
        Set<EmployeeGroup> employeeGroups = new HashSet<>();
        if (employeeData.getGroupIds() != null) {
            for (Long groupId : employeeData.getGroupIds()) {
                EmployeeGroup group = employeeGroupService.findById(groupId);
                if (group == null) {
                    throw new EmployeeGroupNotFoundException(String.format("Ошибка обработки запроса: не удается найти " +
                            "объект группы пользователя по идентификатору %s", groupId));
                }
                employeeGroups.add(group);
            }
        }
        employee.setGroups(employeeGroups);
        employee.setPersonnelNumber(employeeData.getPersonnelNumber());
        List<Long> rolesData = employeeData.getRoles();
        Set<Role> roles = new HashSet<>();
        if (rolesData != null) {
            rolesData.forEach(roleId -> {
                Role role = roleService.getById(roleId);
                if (role != null) roles.add(role);
            });
        }
        employee.setRoles(new ArrayList<>(roles));
        Segment segment = segmentService.findById(employeeData.getSegmentId());
        if (segment == null) {
            throw new SegmentNotFoundException(String.format("Ошибка обработки запроса: не удается найти объект сегмента по идентификатору %s", employeeData.getSegmentId()));
        }
        employee.setSegment(segment);
    }

    private static EmployeeDataForList convertUserListInfo(Employee employee, Boolean lock) {
        EmployeeDataForList res = new EmployeeDataForList();
        res.setId(employee.getId());
        res.setLogin(employee.getLogin());
        res.setFirstName(employee.getFirstName());
        res.setSecondName(employee.getSecondName());
        res.setMiddleName(employee.getMiddleName());
        res.setBirthDate(DateHelper.toLocalDate(employee.getBirthDate()));
        res.setMobilePhone(employee.getMobilePhone());
        res.setEmail(employee.getEmail());
        res.setMobilePhone(employee.getMobilePhone());
        res.setPersonnelNumber(employee.getPersonnelNumber());
        res.setPosition(employee.getPosition());
        res.setFullName(employee.getSecondName() + " " + employee.getFirstName() +
                ((employee.getMiddleName() == null) ? "" : " " + employee.getMiddleName()));
        res.setOffices(employee.getOrgUnits().stream().map(OrgUnit::getName).collect(Collectors.toList()));
        res.setBranches(employee.getOrgUnits().stream()
                .filter(item -> item.getParent() != null)
                .map(OrgUnit::getParent)
                .map(OrgUnit::getName)
                .collect(Collectors.toList()));
        res.setSegment(employee.getSegment().getName());
        List<String> roles = employee.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        res.setRoles(roles.stream().distinct().collect(Collectors.toList()));
        res.setLockStatus(lock);
        if (CollectionUtils.isNotEmpty(employee.getOrgUnits())) {
            res.setOrgUnits(employee.getOrgUnits().stream().map(OrgUnit::getId).collect(Collectors.toList()));
        }
        return res;
    }

    private static EmployeeDataForList convertUserToReportInfo(Employee employee) {
        EmployeeDataForList res = new EmployeeDataForList();
        res.setId(employee.getId());
        res.setLogin(employee.getLogin());
        res.setFirstName(employee.getFirstName());
        res.setSecondName(employee.getSecondName());
        res.setMiddleName(employee.getMiddleName());
        res.setBirthDate(DateHelper.toLocalDate(employee.getBirthDate()));
        res.setMobilePhone(employee.getMobilePhone());
        res.setInnerPhone(employee.getInnerPhone());
        res.setEmail(employee.getEmail());
        res.setPersonnelNumber(employee.getPersonnelNumber());
        res.setPosition(employee.getPosition());
        res.setFullName(employee.getSecondName() + " " + employee.getFirstName() +
                ((employee.getMiddleName() == null) ? "" : " " + employee.getMiddleName()));
        res.setSegment(employee.getSegment().getName());
        return res;
    }

    @ExceptionHandler
    ResponseEntity handleException(final UserOfficeNotFoundException exception) {
        LOGGER.error("Ошибка обработки запроса: офис не найден", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorRes(OFFICE_FIELD_NAME, exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity handleException(final SegmentNotFoundException exception) {
        LOGGER.error("Ошибка обработки запроса: сегмент не найден", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorRes(SEGMENT_ID_FIELD_NAME, exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity handleException(final LoginAlreadyExistException exception) {
        LOGGER.error("Ошибка обработки запроса: пользователь с заданным логином уже существует", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorRes(LOGIN_FIELD_NAME,
                exception.getMessage() != null ? exception.getMessage() : "Пользователь с заданным логином уже существует"));
    }

    @ExceptionHandler
    ResponseEntity handleException(final PasswordMissingException exception) {
        LOGGER.error("Ошибка обработки запроса: необходимо задать пароль для пользователя", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorRes(PASSWORD_FIELD_NAME, exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity handleException(final UserAlreadyLockedException exception) {
        LOGGER.error("Ошибка обработки запроса: пользователь уже заблокирован", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorRes(USER_ID_FIELD_NAME, exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity handleException(final UserNotLockedException exception) {
        LOGGER.error("Ошибка обработки запроса: пользователь не заблокирован или не существует", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorRes(USER_ID_FIELD_NAME, exception.getMessage()));
    }

    @ExceptionHandler
    ResponseEntity handleException(final BadQualityPasswordException exception) {
        LOGGER.error("Ошибка обработки запроса: устанавливаемый пароль не удовлетворяет требованиям к сложности", exception);
        ErrorsDataRs errorsDataRs = new ErrorsDataRs();
        List<ErrorData> errors = exception.getErrors().stream()
                .map(message -> new ErrorData(PASSWORD_FIELD_NAME, message))
                .collect(Collectors.toList());
        errorsDataRs.setErrors(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsDataRs);
    }
}
