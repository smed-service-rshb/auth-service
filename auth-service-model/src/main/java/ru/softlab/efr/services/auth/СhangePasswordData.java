package ru.softlab.efr.services.auth;

import java.io.Serializable;

public class СhangePasswordData implements Serializable {
    private static final long serialVersionUID = -3141271584285134943L;

    private String login;
    private String oldPassword;
    private String newPassword;

    public СhangePasswordData() {
    }

    public СhangePasswordData(String login, String oldPassword, String newPassword) {
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
