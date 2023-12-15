package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CreateSessionRq
 */
@Validated
public class CreateSessionRq  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("login")
    private String login = null;

    @JsonProperty("password")
    private String password = null;

    @JsonProperty("officeId")
    private Long officeId = null;


    /**
     * Создает пустой экземпляр класса
     */
    public CreateSessionRq() {}

    /**
     * Создает экземпляр класса
     * @param login логин
     * @param password пароль
     * @param officeId Идентификатор офиса в который выполняется вход
     */
    public CreateSessionRq(String login, String password, Long officeId) {
        this.login = login;
        this.password = password;
        this.officeId = officeId;
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
     * пароль
    * @return пароль
    **/
      @NotNull



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Идентификатор офиса в который выполняется вход
    * @return Идентификатор офиса в который выполняется вход
    **/
    


    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateSessionRq createSessionRq = (CreateSessionRq) o;
        return Objects.equals(this.login, createSessionRq.login) &&
            Objects.equals(this.password, createSessionRq.password) &&
            Objects.equals(this.officeId, createSessionRq.officeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, officeId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CreateSessionRq {\n");
        
        sb.append("    login: ").append(toIndentedString(login)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    officeId: ").append(toIndentedString(officeId)).append("\n");
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

