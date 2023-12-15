package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ChangePasswordData
 */
@Validated
public class ChangePasswordData  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("login")
    private String login = null;

    @JsonProperty("oldPassword")
    private String oldPassword = null;

    @JsonProperty("newPassword")
    private String newPassword = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ChangePasswordData() {}

    /**
     * Создает экземпляр класса
     * @param login логин
     * @param oldPassword старый пароль
     * @param newPassword новый пароль
     */
    public ChangePasswordData(String login, String oldPassword, String newPassword) {
        this.login = login;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    /**
     * логин
    * @return логин
    **/
      @NotNull



    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    /**
     * старый пароль
    * @return старый пароль
    **/
      @NotNull



    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }


    /**
     * новый пароль
    * @return новый пароль
    **/
      @NotNull



    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChangePasswordData changePasswordData = (ChangePasswordData) o;
        return Objects.equals(this.login, changePasswordData.login) &&
            Objects.equals(this.oldPassword, changePasswordData.oldPassword) &&
            Objects.equals(this.newPassword, changePasswordData.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, oldPassword, newPassword);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ChangePasswordData {\n");
        
        sb.append("    login: ").append(toIndentedString(login)).append("\n");
        sb.append("    oldPassword: ").append(toIndentedString(oldPassword)).append("\n");
        sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
    * Convert the given object to string with each line indented by 4 spaces
    * (except the first line).
    */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
          return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

