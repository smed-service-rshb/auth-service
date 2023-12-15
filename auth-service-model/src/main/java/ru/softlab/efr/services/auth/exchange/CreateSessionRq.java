package ru.softlab.efr.services.auth.exchange;


import org.hibernate.validator.constraints.NotBlank;

import java.util.Objects;

/**
 * Тело запроса на аутентификацию (создание сессии)
 *
 * @author niculichev
 * @since 20.04.2017
 */
public class CreateSessionRq {

    @NotBlank(message = "{CreateSessionRq.login.NotBlank}")
    private String login;
    @NotBlank(message = "{CreateSessionRq.password.NotBlank}")
    private String password;

    /**
     * Конструктор
     */
    public CreateSessionRq() {
    }

    /**
     * Конструктор
     *
     * @param login    логин
     * @param password пароль
     */
    public CreateSessionRq(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Получить логин
     *
     * @return логин
     */
    public String getLogin() {
        return login;
    }

    /**
     * Задать логин
     *
     * @param login логин
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Получить пароль
     *
     * @return пароль
     */
    public String getPassword() {
        return password;
    }

    /**
     * Задать пароль
     *
     * @param password пароль
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateSessionRq that = (CreateSessionRq) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return (login + "@" + password).hashCode();
    }
}
