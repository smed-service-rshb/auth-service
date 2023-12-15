package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PrivateChangeLoginData
 */
@Validated
public class PrivateChangeLoginData  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("oldLogin")
    private String oldLogin = null;

    @JsonProperty("newLogin")
    private String newLogin = null;


    /**
     * Создает пустой экземпляр класса
     */
    public PrivateChangeLoginData() {}

    /**
     * Создает экземпляр класса
     * @param oldLogin Текущий логин пользователя
     * @param newLogin Желаемый логин пользователя
     */
    public PrivateChangeLoginData(String oldLogin, String newLogin) {
        this.oldLogin = oldLogin;
        this.newLogin = newLogin;
    }

    /**
     * Текущий логин пользователя
    * @return Текущий логин пользователя
    **/
      @NotNull



    public String getOldLogin() {
        return oldLogin;
    }

    public void setOldLogin(String oldLogin) {
        this.oldLogin = oldLogin;
    }


    /**
     * Желаемый логин пользователя
    * @return Желаемый логин пользователя
    **/
      @NotNull



    public String getNewLogin() {
        return newLogin;
    }

    public void setNewLogin(String newLogin) {
        this.newLogin = newLogin;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrivateChangeLoginData privateChangeLoginData = (PrivateChangeLoginData) o;
        return Objects.equals(this.oldLogin, privateChangeLoginData.oldLogin) &&
            Objects.equals(this.newLogin, privateChangeLoginData.newLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldLogin, newLogin);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PrivateChangeLoginData {\n");
        
        sb.append("    oldLogin: ").append(toIndentedString(oldLogin)).append("\n");
        sb.append("    newLogin: ").append(toIndentedString(newLogin)).append("\n");
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

