package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.EmployeeDataForList;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * EmployeesData
 */
@Validated
public class EmployeesData  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("employees")
    @Valid
    private List<EmployeeDataForList> employees = null;


    /**
     * Создает пустой экземпляр класса
     */
    public EmployeesData() {}

    /**
     * Создает экземпляр класса
     * @param employees Список сотрудников
     */
    public EmployeesData(List<EmployeeDataForList> employees) {
        this.employees = employees;
    }

    public EmployeesData addEmployeesItem(EmployeeDataForList employeesItem) {
        if (this.employees == null) {
            this.employees = new ArrayList<>();
        }
        this.employees.add(employeesItem);
        return this;
    }

    /**
     * Список сотрудников
    * @return Список сотрудников
    **/
    
  @Valid


    public List<EmployeeDataForList> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDataForList> employees) {
        this.employees = employees;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmployeesData employeesData = (EmployeesData) o;
        return Objects.equals(this.employees, employeesData.employees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employees);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EmployeesData {\n");
        
        sb.append("    employees: ").append(toIndentedString(employees)).append("\n");
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

