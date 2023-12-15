package ru.softlab.efr.services.auth.exchange;

import org.hibernate.validator.constraints.NotBlank;

public class ChangePasswordRq {

    @NotBlank(message = "{CreateSessionRq.login.NotBlank}")
    private String login;
    @NotBlank(message = "{CreateSessionRq.login.NotBlank}")
    private String oldPassword;
    @NotBlank(message = "{CreateSessionRq.login.NotBlank}")
    private String newPassword;

    public ChangePasswordRq() {
    }

    public ChangePasswordRq(String login, String oldPassword, String newPassword) {
        this.login = login;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
