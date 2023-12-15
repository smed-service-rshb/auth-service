package ru.softlab.efr.services.auth.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.exceptions.*;
import ru.softlab.efr.services.auth.model.*;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserRepository;
import ru.softlab.efr.services.auth.utils.model.UserLoginAttemptConfig;
import ru.softlab.efr.services.authorization.PrincipalData;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Сервис, содержащий логику аутентификации
 *
 * @author niculichev
 * @since 12.04.2017
 */
@Service
public class AuthenticateService {

    private static final Logger LOGGER = Logger.getLogger(AuthenticateService.class);

    private SessionStoreService sessionStoreService;
    private UserRepository userRepository;
    private UserLockService userLockService;
    private UserLoginAttemptsService userLoginAttemptsService;

    @Autowired
    public void setSessionStoreService(SessionStoreService sessionStoreService) {
        this.sessionStoreService = sessionStoreService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserLockService(UserLockService userLockService) {
        this.userLockService = userLockService;
    }

    @Autowired
    public void setUserLoginAttemptsService(UserLoginAttemptsService userLoginAttemptsService) {
        this.userLoginAttemptsService = userLoginAttemptsService;
    }

    /**
     * Аутентификация пользователя в соответствии с выбранным методом
     *
     * @param login    логин
     * @param password пароль
     * @return Экземпляр созданной сессии
     * @throws Exception ошибка аутентификации
     */
    public SessionData authenticate(String login, String password) throws Exception {
        Employee authenticate = auth(login, password, null);
        Session session = createSession(authenticate);
        sessionStoreService.save(session);
        return new SessionData(authenticate, session);
    }

    public SessionData authenticateByPasswordHash(String login, String passwordHash) throws Exception {
        Employee authenticate = auth(login, null, passwordHash);
        Session session = createSession(authenticate);
        sessionStoreService.save(session);
        return new SessionData(authenticate, session);
    }

    public Employee auth(String login, String password, String passwordHash) throws UserIdentificationException, UserAuthenticationException, LoginBlockException {
        Employee user = userRepository.findTopByLogin(login);

        if (user == null || user.isDeleted()) {
            throw new UserIdentificationException();
        }
        if (userLockService.isLocked(user.getId())) {
            throw new LoginBlockException();
        }
        if (Objects.isNull(passwordHash)) {
            passwordHash = DigestUtils.sha1Hex(password);
        }
        if (!passwordHash.equals(user.getPasswordHash())) {
            //сохраняем попытку входа с неверно веденным паролем
            userLoginAttemptsService.saveLoginAttempts(user);
            if (userLoginAttemptsService.isMaxAttemptExceeded(user)) {
                UserLoginAttemptConfig configParams = userLoginAttemptsService.getConfigParams();
                Integer timeout = configParams.getWrongPasswordTimeout();
                LocalDateTime dateTimeAnd = timeout > 0 ? LocalDateTime.now().plusSeconds(timeout) : LocalDateTime.now().plusYears(100L);
                try {
                    userLockService.lock(user.getId(), "max attempts count exceeded", LockType.AUTO, dateTimeAnd);
                } catch (UserAlreadyLockedException e) {
                    LOGGER.warn("User with id " + user.getId() + " is already locked");
                }
            }
            throw new UserAuthenticationException();
        }
        //Найти предыдущие неудачные попытки входа и удалить
        userLoginAttemptsService.deleteAllAttemptsByUser(user);
        return user;
    }

    private Session createSession(Employee employee) {
        Session session = new Session();
        session.setCreationDate(new Date());
        session.setState(Session.State.active);
        session.setOwnerId(employee.getId());
        session.setOwnerLogin(employee.getLogin());
        session.setOwnerFirstName(employee.getFirstName());
        session.setOwnerSecondName(employee.getSecondName());
        session.setOwnerMiddleName(employee.getMiddleName());
        session.setOwnerMobilePhone(employee.getMobilePhone());
        session.setOwnerEmail(employee.getEmail());
        session.setOwnerPosition(employee.getPosition());
        if (employee.getOrgUnits().toArray().length == 1) {
            session.setOwnerOffice(employee.getOrgUnits().stream()
                    .map(OrgUnit::getName).findFirst().orElse(null));
            session.setOwnerBranch(employee.getOrgUnits().stream()
                    .map(OrgUnit::getParent)
                    .map(OrgUnit::getName)
                    .findFirst().orElse(null));
        }
        session.setOwnerPersonnelNumber(employee.getPersonnelNumber());
        session.setOwnerRights(getRights(employee));
        session.setChangePassword(employee.isChangePassword());
        return session;
    }

    /**
     * Получение списка прав для сотрудника
     *
     * @param employee сотрудник
     * @return список прав
     */
    public List<Right> getRights(Employee employee) {
        List<Role> roles = employee.getRoles();
        Set<Right> rights = EnumSet.noneOf(Right.class);
        for (Role role : roles) {
            rights.addAll(role.getRights());
        }
        return new ArrayList<>(rights);
    }

    /**
     * Сервис изменения логина клиентом
     *
     * @param newLogin      новый логин
     * @param principalData данные клиента
     * @throws LoginAlreadyExistException  исключение выбрасываемое если новый логин клиента уже есть в системе
     * @throws EntityNotFoundException     исключение выбрасываемое если клиент не найден
     * @throws UserIdentificationException исключение если по данным клиента было найдено более одного клиента
     */
    @Transactional
    public void changeLogin(String newLogin, PrincipalData principalData) throws LoginAlreadyExistException, EntityNotFoundException, UserIdentificationException {
        Employee byLogin = userRepository.findByLogin(newLogin);
        if (byLogin != null) {
            throw new LoginAlreadyExistException();
        }
        List<Employee> users = userRepository.findAllByFilter(principalData.getSecondName(), principalData.getFirstName(), principalData.getMobilePhone());
        if (users == null) {
            throw new EntityNotFoundException();
        }
        if (users.size() > 1) {
            throw new UserIdentificationException();
        }
        Employee user = users.get(0);
        user.setLogin(newLogin);
        userRepository.saveAndFlush(user);
    }

    /**
     * Сервис изменения логина клиентом
     *
     * @param newLogin новый логин
     * @param oldLogin текущий логин клиента
     * @throws LoginAlreadyExistException  исключение выбрасываемое если новый логин клиента уже есть в системе
     * @throws EntityNotFoundException     исключение выбрасываемое если клиент не найден
     */
    @Transactional
    public void changeLogin(String oldLogin, String newLogin) throws LoginAlreadyExistException, EntityNotFoundException {
        Employee byLogin = userRepository.findByLogin(newLogin);
        if (byLogin != null) {
            throw new LoginAlreadyExistException();
        }
        Employee user = userRepository.findByLogin(oldLogin);
        if (user == null) {
            throw new EntityNotFoundException();
        }
        user.setLogin(newLogin);
        userRepository.saveAndFlush(user);
    }
}
