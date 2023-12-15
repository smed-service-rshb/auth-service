package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SetPasswordData
 */
@Validated
public class SetPasswordData  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("newPassword")
    private String newPassword = null;


    /**
     * Создает пустой экземпляр класса
     */
    public SetPasswordData() {}

    /**
     * Создает экземпляр класса
     * @param newPassword Пароль
     */
    public SetPasswordData(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Пароль
    * @return Пароль
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
        SetPasswordData setPasswordData = (SetPasswordData) o;
        return Objects.equals(this.newPassword, setPasswordData.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newPassword);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SetPasswordData {\n");
        
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

