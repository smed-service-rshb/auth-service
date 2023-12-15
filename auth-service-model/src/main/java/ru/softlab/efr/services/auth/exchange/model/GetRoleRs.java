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
 * GetRoleRs
 */
@Validated
public class GetRoleRs  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("desc")
    private String desc = null;

    @JsonProperty("rights")
    @Valid
    private List<String> rights = null;


    /**
     * Создает пустой экземпляр класса
     */
    public GetRoleRs() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор роли
     * @param name Название роли
     * @param desc Краткое описание(для отображения пользователю)
     * @param rights Назначенные права
     */
    public GetRoleRs(Long id, String name, String desc, List<String> rights) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.rights = rights;
    }

    /**
     * Идентификатор роли
    * @return Идентификатор роли
    **/
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Название роли
    * @return Название роли
    **/
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Краткое описание(для отображения пользователю)
    * @return Краткое описание(для отображения пользователю)
    **/
    


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public GetRoleRs addRightsItem(String rightsItem) {
        if (this.rights == null) {
            this.rights = new ArrayList<>();
        }
        this.rights.add(rightsItem);
        return this;
    }

    /**
     * Назначенные права
    * @return Назначенные права
    **/
    


    public List<String> getRights() {
        return rights;
    }

    public void setRights(List<String> rights) {
        this.rights = rights;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetRoleRs getRoleRs = (GetRoleRs) o;
        return Objects.equals(this.id, getRoleRs.id) &&
            Objects.equals(this.name, getRoleRs.name) &&
            Objects.equals(this.desc, getRoleRs.desc) &&
            Objects.equals(this.rights, getRoleRs.rights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc, rights);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GetRoleRs {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    desc: ").append(toIndentedString(desc)).append("\n");
        sb.append("    rights: ").append(toIndentedString(rights)).append("\n");
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

