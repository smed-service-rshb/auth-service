package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация об установке короткого кода 
 */
@Validated
public class ShortCodeResponse  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("token")
    private String token = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ShortCodeResponse() {}

    /**
     * Создает экземпляр класса
     * @param token Токен мобильного устройства на сервере 
     */
    public ShortCodeResponse(String token) {
        this.token = token;
    }

    /**
     * Токен мобильного устройства на сервере 
    * @return Токен мобильного устройства на сервере 
    **/
      @NotNull



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShortCodeResponse shortCodeResponse = (ShortCodeResponse) o;
        return Objects.equals(this.token, shortCodeResponse.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ShortCodeResponse {\n");
        
        sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

