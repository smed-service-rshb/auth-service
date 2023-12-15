package ru.softlab.efr.services.auth.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.auth.exceptions.*;
import ru.softlab.efr.services.auth.exchange.model.MotivationCorrectStatus;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.OrgUnit;
import ru.softlab.efr.services.auth.model.Role;
import ru.softlab.efr.services.auth.model.Segment;
import ru.softlab.efr.services.auth.services.impl.CurrentStateService;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.MotivationEmployeeSearchRepository;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserRepository;
import ru.softlab.efr.services.auth.utils.EmployeeXLSXReader;
import ru.softlab.efr.services.auth.utils.model.EmployeeParseResult;
import ru.softlab.efr.services.auth.utils.model.ParseError;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.*;

import static ru.softlab.efr.services.auth.services.impl.dao.repositories.EmployeeSearchSpecification.employeeSpecification;

/**
 * Сервис работы с сотрудником
 *
 * @author akrenev
 * @since 19.03.2018
 */
@Service

@PropertySource("classpath:update.properties")
public class EmployeeService {

    private static final Logger LOGGER = Logger.getLogger(EmployeeService.class);
    private static final String DEFAULT_USER_ROLE = "Пользователь";
    private static final String SEGMENT_RETAIL_CODE = "retail";
    private static final String SEGMENT_VIP_CODE = "vip";

    private static final Sort USER_LIST_SORT_BY_NAME = new Sort(Sort.Direction.ASC, "secondName", "firstName", "middleName");

    @Autowired
    private UserStoreService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MotivationEmployeeSearchRepository motivationEmployeeSearchRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private CurrentStateService currentStateService;


    @Autowired
    private OrgUnitService orgUnitService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private SegmentService segmentService;

    @Autowired
    @Qualifier("roleServiceDaoImpl")
    private RoleStoreService roleService;

    @Value("${list.update.start.ok}")
    private String startUpdateOk;

    @Value("${list.update.finished}")
    private String updateFinished;

    @Value("${list.update.start.fail}")
    private String startUpdateFail;

    @Value("${list.update.start.already}")
    private String startUpdateAlready;

    @Value("${list.update.status.running}")
    private String statusIsRunning;

    @Value("${list.update.status.stop}")
    private String statusIsNotRunning;

    @Value("${employee.filepath}")
    private String filepath;

    @Value("${employee.sheetname}")
    private String sheetName;

    @Value("${UserData.login.already.exists}")
    private String loginAlreadyExistsMsg;

    @Value("${UserData.password.missing}")
    private String passwordMissingMsg;

    @Value("${employee.update.error}")
    private String employeeUpdateError;

    @Value("${employee.parse.error}")
    private String employeeParseError;

    @Value("${employee.create.error}")
    private String employeeInsertError;

    /**
     * Получение полного списка сотрудников
     *
     * @return сотрудники
     */
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return userRepository.findAll(USER_LIST_SORT_BY_NAME);
    }

    /**
     * Получение полного списка сотрудников с пагинацией
     *
     * @param deleted  false-только удаленные
     * @param pageable постранично
     * @return сотрудники
     */
    @Transactional(readOnly = true)
    public Page<Employee> findAll(boolean deleted, Pageable pageable) {
        return userRepository.findAllByDeleted(deleted, pageable);
    }


    /**
     * Получение полного списка сотрудников с пагинацией и фильтрацией
     *
     * @param secondName      Фамилия сотрудника
     * @param firstName       Имя сотрудника
     * @param middleName      Отчество сотрудника
     * @param personnelNumber Табельный номер
     * @param branches        Название регионального филиала
     * @param orgUnitIdes     Список идентификаторов ВСП
     * @return сотрудники
     */
    @Transactional(readOnly = true)
    public Page<Employee> findAllWithFilter(boolean deleted, Pageable pageable,
                                            String secondName, String firstName, String middleName,
                                            String personnelNumber, List<Long> orgUnitIdes, List<String> branches,
                                            String position, Long roleId) {
        return userRepository.findAll(employeeSpecification(secondName,
                firstName, middleName, personnelNumber, branches, orgUnitIdes, position, roleId), pageable);
    }

    /**
     * Получение полного списка сотрудников с пагинацией и фильтрацией
     *
     * @param secondName      Фамилия сотрудника
     * @param firstName       Имя сотрудника
     * @param middleName      Отчество сотрудника
     * @param personnelNumber Табельный номер
     * @param branches        Название регионального филиала
     * @param orgUnitIdes     Список идентификаторов ВСП
     * @return сотрудники
     */
    @Transactional(readOnly = true)
    public Page<Employee> findAllWithFilterAndWithMotivationCheck(boolean deleted, Pageable pageable,
                                                                  String secondName, String firstName, String middleName,
                                                                  String personnelNumber, List<Long> orgUnitIdes, List<String> branches,
                                                                  String position, Long roleId, MotivationCorrectStatus motivationCorrectStatus) {
        return motivationEmployeeSearchRepository.getFilteredRequestsRq(pageable, secondName, firstName, middleName,
                personnelNumber, orgUnitIdes, branches,
                position, roleId, motivationCorrectStatus);
    }

    /**
     * Получение полного списка сотрудников с пагинацией и фильтрацией
     *
     * @param surname     Фамилия сотрудника
     * @param firstName   имя сотрудника
     * @param birthDate   Дата рождения
     * @param mobilePhone Номер мобильного телефона
     * @return пользователи
     */
    @Transactional(readOnly = true)
    public List<Employee> findAllByFilter(String surname, String firstName, Date birthDate, String mobilePhone) {
        return userRepository.findAllByFilter(surname, firstName, birthDate, mobilePhone);
    }

    /**
     * Получение полного списка сотрудников включая удаленных для построения отчета
     *
     * @return пользователи
     */
    @Transactional(readOnly = true)
    public List<Employee> findAllWithDeleted() {
        return userRepository.findAll();
    }

    /**
     * Получение сотрудника по идентификатору
     *
     * @param id - идентификатор сотрудника
     * @return сотрудник
     */
    public Employee findById(Long id) {
        return userService.getById(id);
    }


    /**
     * Получение неудалённого сотрудника по идентификатору
     *
     * @param id идентификатор сотрудника
     * @return сотрудник
     */
    public Employee findByIdAndNotDeleted(Long id) {
        return userService.getByIdNotDeleted(id);
    }

    /**
     * Получение сотрудника по логину
     *
     * @param login логин
     * @return сотрудник
     */
    public Employee findByLogin(String login) {
        return userService.getByLogin(login);
    }

    /**
     * Поиск сотрудников по офису
     *
     * @param orgUnitId id ОШС
     * @return сотрудники
     * @throws ConnectActiveDirectoryException, UserOfficeNotFoundException, UserWithoutRoleException, UserRoleNotFoundException, SystemException
     */
    public List<Employee> findByOffice(Long orgUnitId) throws ConnectActiveDirectoryException, UserOfficeNotFoundException, UserWithoutRoleException, UserRoleNotFoundException, SystemException {
        return userRepository.findEmployeesByOrgUnitsIdAndDeleted(orgUnitId, Boolean.FALSE);
    }

    /**
     * Поиск сотрудника по идентифиактору и офису
     *
     * @param personnelNumber идентификатор
     * @param orgUnitId       id офиса
     * @return сотрудники
     */
    public Employee findByIdAndOffice(String personnelNumber, Long orgUnitId) throws ConnectActiveDirectoryException, UserOfficeNotFoundException, UserWithoutRoleException, UserRoleNotFoundException, SystemException, AmbiguousEntityException, ru.softlab.efr.services.auth.exceptions.EntityNotFoundException {
        return userRepository.findEmployeeByPersonnelNumberAndOrgUnitsId(personnelNumber, orgUnitId);
    }

    /**
     * Создать объект сотрудника
     *
     * @param employee данные сотрудника
     * @return сохраненный объект сотрудника
     */
    @Transactional(rollbackFor = EntityExistsException.class)
    public Employee create(Employee employee) throws LoginAlreadyExistException, PasswordMissingException {
        if (employee.getId() != null && userRepository.findOne(employee.getId()) != null) {
            throw new EntityExistsException();
        }
        if (userRepository.findTopByLogin(employee.getLogin()) != null) {
            throw new LoginAlreadyExistException(String.format(loginAlreadyExistsMsg, employee.getLogin()));
        }
        //Генерируем новый пароль если пароль не установлен
        if (employee.getPasswordHash() == null) {
            throw new PasswordMissingException(passwordMissingMsg);
        }
        employee.setChangePassword(true);
        return userRepository.saveAndFlush(employee);
    }

    /**
     * Создать объект сотрудника
     *
     * @param employee данные сотрудника
     * @return сохраненный объект сотрудника
     */
    @Transactional(rollbackFor = EntityExistsException.class)
    public Employee createBySelfRegistration(Employee employee) throws LoginAlreadyExistException, PasswordMissingException {
        if (employee.getId() != null && userRepository.findOne(employee.getId()) != null) {
            throw new EntityExistsException();
        }
        if (userRepository.findTopByLogin(employee.getLogin()) != null) {
            throw new LoginAlreadyExistException(String.format(loginAlreadyExistsMsg, employee.getLogin()));
        }
        //Генерируем новый пароль если пароль не установлен
        if (employee.getPasswordHash() == null) {
            throw new PasswordMissingException(passwordMissingMsg);
        }
        return userRepository.saveAndFlush(employee);
    }

    /**
     * Сделать пароль сотрудника = логину, установить флаг обязательной смены пароля при авторизации.
     *
     * @param id id сотрудника
     */
    @Transactional
    public void resetPassword(Long id) {
        Employee employee = userRepository.findById(id);
        if (employee == null) {
            throw new EntityNotFoundException();
        }
        employee.setPasswordHash(passwordService.getPasswordHash(employee.getPersonnelNumber()));
        employee.setChangePassword(true);
        userRepository.saveAndFlush(employee);
    }

    /**
     * Установка клиенту технического пароля
     *
     * @param id          идентификатор клиента
     * @param newPassword технический пароль
     * @throws ru.softlab.efr.services.auth.exceptions.EntityNotFoundException
     */
    public void resetClientPassword(Long id, String newPassword) throws ru.softlab.efr.services.auth.exceptions.EntityNotFoundException {
        Employee client = userRepository.findById(id);
        if (client == null) {
            throw new ru.softlab.efr.services.auth.exceptions.EntityNotFoundException();
        }
        client.setPasswordHash(passwordService.getPasswordHash(newPassword));
        client.setChangePassword(true);
        userRepository.saveAndFlush(client);
    }

    /**
     * Обновление информации по сотруднику.
     *
     * @param employee сотрудник
     * @return обновлённый сотрудник
     */
    @Transactional
    public Employee update(Employee employee) throws LoginAlreadyExistException {
        Employee existedEntity = userRepository.findOne(employee.getId());
        if (existedEntity == null) {
            throw new EntityNotFoundException();
        }
        Employee userWithLogin = userRepository.findTopByLogin(employee.getLogin());
        if (userWithLogin != null && !userWithLogin.getId().equals(employee.getId())) {
            throw new LoginAlreadyExistException(String.format(loginAlreadyExistsMsg, employee.getLogin()));
        }
        return userRepository.saveAndFlush(employee);
    }

    /**
     * Логическое удаление сотрудника
     *
     * @param employeeId - идентификатор сотрудника
     */
    @Transactional
    public boolean logicalDelete(Long employeeId) {
        Employee employee = userRepository.findOne(employeeId);
        if (employee == null || employee.isDeleted()) {
            throw new EntityNotFoundException();
        }
        employee.setDeleted(true);
        userRepository.saveAndFlush(employee);
        return true;
    }

    /**
     * Обновление списка сотрудников из файла
     *
     * @return результат выполнения
     */

    public String updateList() {
        if (currentStateService.isRunning("EmployeesUpdate")) {
            return startUpdateAlready;
        }
        try {
            taskExecutor.execute(this::runSync);
            return startUpdateOk;

        } catch (Exception ex) {
            LOGGER.error("Произошла ошибка во обновления списка сотрудников, причина:", ex);
            return startUpdateFail;
        }
    }

    public String getUpdateStatus() {
        return currentStateService.getStatus("EmployeesUpdate");
    }

    private OrgUnit findOrgUnit(String name) {
        try {
            return orgUnitService.findById(Long.valueOf("200" + name));
        } catch (Exception ex) {
            LOGGER.error(String.format("Произошла ошибка во время поиска орг-структуры по ID%s, причина:", "200" + name), ex);
        }
        return null;
    }

    private Segment findSegment(String name) {
        Segment defaultSegment = segmentService.findByCode(SEGMENT_RETAIL_CODE);
        if (name == null || name.isEmpty()) {
            return defaultSegment;
        }
        String code = name.equals("1") ? SEGMENT_VIP_CODE : SEGMENT_RETAIL_CODE;
        Segment result = segmentService.findByCode(code);
        if (result == null) {
            return defaultSegment;
        }
        return result;
    }

    private Role findRole(String name) {
        Role defaultRole = roleService.getByName(DEFAULT_USER_ROLE);
        if (name == null || name.isEmpty()) {
            return defaultRole;
        }
        return roleService.getByName(name);
    }

    private boolean isNotEmpty(String input) {
        return input != null && !input.isEmpty();
    }

    private Employee copyNotEmptyProperties(Employee employeeXLSX, Employee employeeDB) {
        if (isNotEmpty(employeeXLSX.getSecondName())) {
            employeeDB.setSecondName(employeeXLSX.getSecondName());
        }
        if (isNotEmpty(employeeXLSX.getFirstName())) {
            employeeDB.setFirstName(employeeXLSX.getFirstName());
        }
        if (isNotEmpty(employeeXLSX.getMiddleName())) {
            employeeDB.setMiddleName(employeeXLSX.getMiddleName());
        }
        if (isNotEmpty(employeeXLSX.getEmail())) {
            employeeDB.setEmail(employeeXLSX.getEmail());
        }
        if (isNotEmpty(employeeXLSX.getMobilePhone())) {
            employeeDB.setMobilePhone(employeeXLSX.getMobilePhone());
        }
        if (isNotEmpty(employeeXLSX.getPosition())) {
            employeeDB.setPosition(employeeXLSX.getPosition());
        }
        if (employeeXLSX.getOrgUnits() != null) {
            if (employeeDB.getOrgUnits() == null) {
                employeeDB.setOrgUnits(employeeXLSX.getOrgUnits());
            } else {
                Set<OrgUnit> orgUnits = new HashSet<>();
                employeeXLSX.getOrgUnits().forEach(orgUnit -> {
                    OrgUnit existedOrgUnit = employeeDB.getOrgUnits().stream()
                            .filter(existed -> existed.getId().equals(orgUnit.getId()))
                            .findFirst()
                            .orElse(null);
                    if (existedOrgUnit != null) {
                        orgUnits.add(existedOrgUnit);
                    } else {
                        orgUnits.add(orgUnit);
                    }
                });
                employeeDB.getOrgUnits().clear();
                employeeDB.getOrgUnits().addAll(orgUnits);
            }
        }
        employeeDB.setSegment(employeeXLSX.getSegment());
        employeeDB.setRoles(employeeXLSX.getRoles());
        return employeeDB;
    }

    /*TODO: Доработать, исправить замечания:
            Возвращаемый результат должен быть enum
            Вот здесь currentStateService.isRunning("EmployeesUpdate") и далее в качестве параметра надо передавать enum
            В целом я бы все что связано с репликацией пользователей вынес в отдельный сервис типа EmployeeImportService
            т.к. существующий сервис перегружен ответственностями
            юнит тестов вроде тоже нет
        * */
    @Scheduled(cron = "${employee.update.schedule.cron}")
    void runSync() {
        currentStateService.start("EmployeesUpdate", startUpdateOk);
        try {
            /* STAGE 1: PARSE USER XLSX FILE */
            EmployeeParseResult employeeParseResult = EmployeeXLSXReader.getEmployeeListFromXLSX(filepath, sheetName,
                    this::findOrgUnit,
                    this::findSegment,
                    this::findRole);

            List<Employee> employeesFromXLSX = employeeParseResult.getParsedEmployees();

            employeesFromXLSX.forEach(employeeXLSX -> {
                Employee employeeDB = userRepository.findTopByLogin(employeeXLSX.getPersonnelNumber());
                Long excelRowNum = employeeXLSX.getId();
                StringJoiner errors = getErrors(employeeXLSX);
                if (errors.length() > 0) {
                    employeeParseResult.getErrors().add(new ParseError(excelRowNum, String.format(employeeUpdateError,
                            errors.toString())));
                    LOGGER.info(String.format(employeeUpdateError,
                            excelRowNum + ", " + errors.toString()));
                }

                if (employeeDB != null) {
                    /* STAGE 2: UPDATE EXIST USERS */
                    try {
                        update(copyNotEmptyProperties(employeeXLSX, employeeDB));
                    } catch (Exception ex) {
                        employeeParseResult.getErrors().add(new ParseError(excelRowNum, String.format(employeeUpdateError, ex.getMessage())));
                        LOGGER.error(ex);
                    }
                } else {
                    /* STAGE 3: INSERT NEW USERS */
                    try {
                        employeeXLSX.setPasswordHash(passwordService.getPasswordHash(employeeXLSX.getPersonnelNumber()));
                        employeeXLSX.setChangePassword(true);
                        if (errors.length() == 0) {
                            employeeXLSX.setId(null);
                            create(employeeXLSX);
                        }
                    } catch (Exception ex) {
                        employeeParseResult.getErrors().add(new ParseError(excelRowNum, String.format(employeeInsertError, ex.getMessage())));
                        LOGGER.error(ex);
                    }
                }
            });

            if (employeeParseResult.getErrors().size() > 0) {
                try {
                    EmployeeXLSXReader.setSyncErrors2XLSX(filepath, sheetName, employeeParseResult.getErrors());
                    currentStateService.stopSuccess("EmployeesUpdate", String.format(updateFinished, "Employees"));
                } catch (Exception e) {
                    currentStateService.stopFail("EmployeesUpdate", e.getMessage());
                }
            } else {
                currentStateService.stopSuccess("EmployeesUpdate", String.format(updateFinished, "Employees"));
            }
        } catch (Exception e) {
            currentStateService.stopFail("EmployeesUpdate", e.getMessage());
            LOGGER.error(e);
        }

    }

    private StringJoiner getErrors(Employee employee) {
        StringJoiner errors = new StringJoiner(", ");
        if (employee.getOrgUnits() == null) {
            errors.add("Не удалось найти орг.структуру");
        }
        if (employee.getSegment() == null) {
            errors.add("Не удалось найти сегмент");
        }
        if (employee.getRoles() == null
                || employee.getRoles().size() == 0
                || employee.getRoles().get(0) == null) {
            errors.add("Не удалось найти роль");
        }
        return errors;
    }
}
