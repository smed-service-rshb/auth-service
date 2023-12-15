package ru.softlab.efr.services.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;
import ru.softlab.efr.infrastructure.logging.api.model.OperationMode;
import ru.softlab.efr.infrastructure.logging.api.model.OperationState;
import ru.softlab.efr.infrastructure.logging.api.services.OperationLogService;
import ru.softlab.efr.services.auth.exceptions.UserAlreadyLockedException;
import ru.softlab.efr.services.auth.exceptions.UserNotLockedException;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.LockType;
import ru.softlab.efr.services.auth.model.UserLock;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserLockRepository;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

/**
 * Сервис для управления блокировками пользователя
 */
@Service
public class UserLockService {

    private UserRepository userRepository;
    private UserLockRepository userLockRepository;
    private OperationLogService operationLogService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserLockRepository(UserLockRepository userLockRepository) {
        this.userLockRepository = userLockRepository;
    }

    @Autowired
    public void setOperationLogService(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * Блокировка пользователя
     * @param employeeId ID пользователя
     * @param description описание причины блокировки
     * @return возвращает объект блокировки
     */
    @Transactional(rollbackFor = {EntityNotFoundException.class, UserAlreadyLockedException.class})
    public UserLock lock(Long employeeId, String description, LockType lockType) throws UserAlreadyLockedException {
        return lock(employeeId, description, lockType, null);
    }

    /**
     * Блокировка пользователя
     * @param employeeId ID пользователя
     * @param description описание причины блокировки
     * @return возвращает объект блокировки
     */
    @Transactional(rollbackFor = {EntityNotFoundException.class, UserAlreadyLockedException.class})
    public UserLock lock(Long employeeId, String description, LockType lockType, LocalDateTime endDateTime) throws UserAlreadyLockedException {
        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("LOCK_EMPLOYEE");
        operationLogEntry.setOperationDescription("Блокировка учётной записи сотрудника");
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);
        operationLogEntry.setOperationParameter("userId", employeeId);
        operationLogEntry.setOperationParameter("lockType", lockType.name());
        operationLogEntry.setOperationParameter("description", description);

        try {
            Employee employee = userRepository.findOne(employeeId);
            if (employee == null || employee.isDeleted()) {
                throw new EntityNotFoundException();
            }
            if (LockType.BY_HAND == lockType && isLockedByHand(employeeId)) {  //ручная блокировка
                throw new UserAlreadyLockedException(String.format("Пользователь с id=%s уже заблокирован", employeeId));
            }
            if (LockType.AUTO == lockType && isLocked(employeeId)) {
                throw new UserAlreadyLockedException(String.format("Пользователь с id=%s уже заблокирован", employeeId));
            }
            UserLock userLock = new UserLock();
            userLock.setCreationDate(LocalDateTime.now());
            userLock.setUser(employee);
            userLock.setDescription(description);
            userLock.setLocked(true);
            userLock.setType(lockType);
            userLock.setEndDateTime(endDateTime);
            userLockRepository.save(userLock);
            operationLogEntry.setOperationState(OperationState.SUCCESS);
            return userLock;
        } catch (Exception ex) {
            operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
            operationLogEntry.setOperationParameter("error", ex.getMessage());
            throw ex;
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);
        }
    }

    /**
     * разблокировка пользователя
     * @param employeeId ID пользователя
     * @return возвращает объект блокировки
     */
    @Transactional(rollbackFor = {EntityNotFoundException.class, UserAlreadyLockedException.class})
    public void unlock(Long employeeId) throws UserNotLockedException {
        OperationLogEntry operationLogEntry = operationLogService.startLogging();
        operationLogEntry.setOperationKey("UNLOCK_EMPLOYEE");
        operationLogEntry.setOperationDescription("Разблокировка учётной записи сотрудника");
        operationLogEntry.setOperationMode(OperationMode.ACTIVE);
        operationLogEntry.setOperationParameter("userId", employeeId);

        try {
            List<UserLock> allLocks = userLockRepository.findAllByUserIdAndLocked(employeeId, true);
            if (CollectionUtils.isEmpty(allLocks)) {
                throw new UserNotLockedException(String.format("Пользователь с id=%s не заблокирован или не существует", employeeId));
            }
            allLocks.stream().forEach(lockInfo -> {
                lockInfo.setLocked(false);
                userLockRepository.save(lockInfo);
            });
            operationLogEntry.setOperationState(OperationState.SUCCESS);
        } catch (Exception ex) {
            operationLogEntry.setOperationState(OperationState.CLIENT_ERROR);
            operationLogEntry.setOperationParameter("error", ex.getMessage());
            throw ex;
        } finally {
            operationLogEntry.setDuration(Calendar.getInstance().getTimeInMillis() - operationLogEntry.getLogDate().getTimeInMillis());
            operationLogService.log(operationLogEntry);
        }
    }

    /**
     * Проверить заблокирован ли пользователь на текущий момент
     */
    public Boolean isLocked(Long employeeId) {
        List<UserLock> allLocks = userLockRepository.findAllByUserIdAndLocked(employeeId, true);
        return !CollectionUtils.isEmpty(allLocks);
    }

    /**
     * Проверить есть ли ручная блокировка
     */
    private Boolean isLockedByHand(Long employeeId) {
        UserLock lockInfo = userLockRepository.findFirstByUserIdAndLockedAndEndDateTimeIsNull(employeeId, true);
        return lockInfo != null;
    }
}
