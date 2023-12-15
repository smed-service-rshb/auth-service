package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import ru.softlab.efr.services.auth.exchange.model.CreateSessionRsUser;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CreateSessionRs
 */
@Validated
public class CreateSessionRs  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private String id = null;

    @JsonProperty("user")
    private CreateSessionRsUser user = null;


    /**
     * Создает пустой экземпляр класса
     */
    public CreateSessionRs() {}

    /**
     * Создает экземпляр класса
     * @param id идентификатор сессии
     * @param user 
     */
    public CreateSessionRs(String id, CreateSessionRsUser user) {
        this.id = id;
        this.user = user;
    }

    /**
     * идентификатор сессии
    * @return идентификатор сессии
    **/
    


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    /**
    * Get user
    * @return 
    **/
    
  @Valid


    public CreateSessionRsUser getUser() {
        return user;
    }

    public void setUser(CreateSessionRsUser user) {
        this.user = user;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateSessionRs createSessionRs = (CreateSessionRs) o;
        return Objects.equals(this.id, createSessionRs.id) &&
            Objects.equals(this.user, createSessionRs.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CreateSessionRs {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    user: ").append(toIndentedString(user)).append("\n");
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

