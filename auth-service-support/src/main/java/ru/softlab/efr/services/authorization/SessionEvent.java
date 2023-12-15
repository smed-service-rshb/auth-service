package ru.softlab.efr.services.authorization;

/**
 * Событие, связанне из изменением состояния сессии
 *
 * @author niculichev
 * @since 29.05.2017
 */
public class SessionEvent {
    private Type state;
    private String sessionId;

    public SessionEvent(Type state, String sessionId){
        this.state = state;
        this.sessionId = sessionId;
    }

    public Type getState() {
        return state;
    }

    public String getSessionId() {
        return sessionId;
    }

    public enum Type{
        /**
         * Закрытие сессии
         */
        close;
    }
}
