package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.GroupData;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * EmployeeGroupRs
 */
@Validated
public class EmployeeGroupRs  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("groups")
    @Valid
    private List<GroupData> groups = null;


    /**
     * Создает пустой экземпляр класса
     */
    public EmployeeGroupRs() {}

    /**
     * Создает экземпляр класса
     * @param groups Список всех записей справочника групп пользователей
     */
    public EmployeeGroupRs(List<GroupData> groups) {
        this.groups = groups;
    }

    public EmployeeGroupRs addGroupsItem(GroupData groupsItem) {
        if (this.groups == null) {
            this.groups = new ArrayList<>();
        }
        this.groups.add(groupsItem);
        return this;
    }

    /**
     * Список всех записей справочника групп пользователей
    * @return Список всех записей справочника групп пользователей
    **/
    
  @Valid


    public List<GroupData> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupData> groups) {
        this.groups = groups;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmployeeGroupRs employeeGroupRs = (EmployeeGroupRs) o;
        return Objects.equals(this.groups, employeeGroupRs.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EmployeeGroupRs {\n");
        
        sb.append("    groups: ").append(toIndentedString(groups)).append("\n");
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

