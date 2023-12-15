package ru.softlab.efr.services.auth.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.model.CurrentState;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.CurrentStateRepository;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Сервис работы с таблицей статусов выполнения операций
 *
 * @author olshansky
 * @since 21.09.2018.
 */
@Service
public class CurrentStateService {

    @Value("${nothing.to.show}")
    private String resultIsEmpty;

    @Autowired
    private CurrentStateRepository currentStateRepository;

    /**
     * Получить последнее состояние выполнения операции по имени
     *
     * @return состояние выполнения операции
     */
    public CurrentState findCurrentStateByName(String name) {
        return getDefaultCurrentState(currentStateRepository.findTopByNameOrderByModifiedDesc(name), name);
    }

    /**
     * Получить последнее состояние выполнения операции по имени и статусу завершения
     *
     * @return состояние выполнения операции
     */
    public CurrentState findCurrentStateByNameAndFinished(String name, Boolean isFinished) {
        return getDefaultCurrentState(currentStateRepository.findTopByNameAndIsFinishedOrderByIdDesc(name, isFinished), name);
    }

    private CurrentState getDefaultCurrentState(CurrentState currentState, String name) {
        if (currentState == null) {
            currentState = new CurrentState();
            currentState.setName(name);
            currentState.setFinished(true);
        }
        return currentState;
    }

    /**
     * Установить состояние выполнения операции
     *
     */
    public void set(CurrentState currentState) {
        currentStateRepository.save(currentState);
    }

    /**
     * Установить состояние выполнения операции
     *
     */
    public void set(String name, Boolean isFinished, String stateCode, String stateDescription) {
        CurrentState currentState = getDefaultCurrentState(findCurrentStateByNameAndFinished(name, false), name);
        currentState.setName(name);
        currentState.setFinished(isFinished);
        currentState.setStateCode(stateCode);
        currentState.setStateDescription(stateDescription);
        set(currentState);
    }

    /**
     * Проверить выполнен ли процесс
     *
     * @return boolean
     */
    public boolean isRunning(String name) {
        return !findCurrentStateByName(name).getFinished();
    }

    /**
     * Запустить состояние выполнения операции
     *
     */
    public void start(String name, String stateDescription) {
        set(name, false, "START", stateDescription);
    }

    /**
     * Остановить состояние выполнения операции и пометить, как успех
     *
     */
    public void stopSuccess(String name, String stateDescription) {
        set(name, true, "SUCCESS", stateDescription);
    }

    /**
     * Остановить состояние выполнения операции из-за ошибки
     *
     */
    public void stopFail(String name, String stateDescription) {
        set(name, true, "FAIL", stateDescription);
    }

    /**
     * Получить человеческое описание текущего состояния выполнения операции
     *
     * @return string
     */
    public String getStatus(String name) {
        CurrentState currentState = findCurrentStateByName(name);
        if (currentState.getStateCode() == null) {
            return resultIsEmpty;
        }
        Timestamp date = new Timestamp(new Date().getTime());

        if ( currentState.getModified() != null ) {
            date = currentState.getModified();
        } else if (currentState.getCreated() != null) {
                date = currentState.getCreated();
        }

        return String.format("%s - (%s) %s", date, currentState.getStateCode(), currentState.getStateDescription());
    }
}
