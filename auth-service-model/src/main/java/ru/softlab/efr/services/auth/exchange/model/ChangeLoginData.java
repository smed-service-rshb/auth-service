package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ChangeLoginData
 */
@Validated
public class ChangeLoginData  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("login")
    private String login = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ChangeLoginData() {}

    /**
     * Создает экземпляр класса
     * @param login логин
     */
    public ChangeLoginData(String login) {
        this.login = login;
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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChangeLoginData changeLoginData = (ChangeLoginData) o;
        return Objects.equals(this.login, changeLoginData.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ChangeLoginData {\n");
        
        sb.append("    login: ").append(toIndentedString(login)).append("\n");
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

