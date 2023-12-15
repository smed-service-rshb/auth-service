package ru.softlab.efr.services.auth.exchange;

import ru.softlab.efr.services.auth.UserData;

/**
 * Тело ответа на запрос аутентификации
 *
 * @author niculichev
 * @since 20.04.2017
 */
public class CreateSessionRs {
    private String id;
    private UserData user;

    public CreateSessionRs(){
    }

    public CreateSessionRs(String id, UserData user){
        this.id = id;
        this.user = user;
    }

    /**
     * @return уникальный идентификатор сессии
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return данные пользователя
     */
    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }
}
