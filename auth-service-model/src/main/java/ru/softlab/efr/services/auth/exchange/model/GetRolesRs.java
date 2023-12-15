package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.RoleData;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GetRolesRs
 */
@Validated
public class GetRolesRs  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("roles")
    @Valid
    private List<RoleData> roles = null;


    /**
     * Создает пустой экземпляр класса
     */
    public GetRolesRs() {}

    /**
     * Создает экземпляр класса
     * @param roles 
     */
    public GetRolesRs(List<RoleData> roles) {
        this.roles = roles;
    }

    public GetRolesRs addRolesItem(RoleData rolesItem) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(rolesItem);
        return this;
    }

    /**
    * Get roles
    * @return 
    **/
    
  @Valid


    public List<RoleData> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleData> roles) {
        this.roles = roles;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetRolesRs getRolesRs = (GetRolesRs) o;
        return Objects.equals(this.roles, getRolesRs.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roles);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GetRolesRs {\n");
        
        sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
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

