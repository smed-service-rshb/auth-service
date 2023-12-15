package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.MotivationCorrectStatus;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * FilterEmployeesRq
 */
@Validated
public class FilterEmployeesRq  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("secondName")
    private String secondName = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("middleName")
    private String middleName = null;

    @JsonProperty("birthDate")
    private LocalDate birthDate = null;

    @JsonProperty("personnelNumber")
    private String personnelNumber = null;

    @JsonProperty("mobilePhone")
    private String mobilePhone = null;

    @JsonProperty("branches")
    @Valid
    private List<String> branches = null;

    @JsonProperty("orgUnitIdes")
    @Valid
    private List<Long> orgUnitIdes = null;

    @JsonProperty("position")
    private String position = null;

    @JsonProperty("roleId")
    private Long roleId = null;

    @JsonProperty("motivationCorrectStatus")
    private MotivationCorrectStatus motivationCorrectStatus = null;


    /**
     * Создает пустой экземпляр класса
     */
    public FilterEmployeesRq() {}

    /**
     * Создает экземпляр класса
     * @param secondName Фамилия сотрудника
     * @param firstName Имя сотрудника
     * @param middleName Отчество сотрудника
     * @param birthDate Дата рождения
     * @param personnelNumber Табельный номер
     * @param mobilePhone Номер мобильного телефона
     * @param branches Название регионального филиала
     * @param orgUnitIdes Название регионального филиала
     * @param position Должность сотрудника
     * @param roleId Роль сотрудника
     * @param motivationCorrectStatus Статус корректности мотивации
     */
    public FilterEmployeesRq(String secondName, String firstName, String middleName, LocalDate birthDate, String personnelNumber, String mobilePhone, List<String> branches, List<Long> orgUnitIdes, String position, Long roleId, MotivationCorrectStatus motivationCorrectStatus) {
        this.secondName = secondName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.personnelNumber = personnelNumber;
        this.mobilePhone = mobilePhone;
        this.branches = branches;
        this.orgUnitIdes = orgUnitIdes;
        this.position = position;
        this.roleId = roleId;
        this.motivationCorrectStatus = motivationCorrectStatus;
    }

    /**
     * Фамилия сотрудника
    * @return Фамилия сотрудника
    **/
    


    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }


    /**
     * Имя сотрудника
    * @return Имя сотрудника
    **/
    


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    /**
     * Отчество сотрудника
    * @return Отчество сотрудника
    **/
    


    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    /**
     * Дата рождения
    * @return Дата рождения
    **/
    
  @Valid


    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


    /**
     * Табельный номер
    * @return Табельный номер
    **/
    


    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
    }


    /**
     * Номер мобильного телефона
    * @return Номер мобильного телефона
    **/
    


    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }


    public FilterEmployeesRq addBranchesItem(String branchesItem) {
        if (this.branches == null) {
            this.branches = new ArrayList<>();
        }
        this.branches.add(branchesItem);
        return this;
    }

    /**
     * Название регионального филиала
    * @return Название регионального филиала
    **/
    


    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> branches) {
        this.branches = branches;
    }


    public FilterEmployeesRq addOrgUnitIdesItem(Long orgUnitIdesItem) {
        if (this.orgUnitIdes == null) {
            this.orgUnitIdes = new ArrayList<>();
        }
        this.orgUnitIdes.add(orgUnitIdesItem);
        return this;
    }

    /**
     * Название регионального филиала
    * @return Название регионального филиала
    **/
    


    public List<Long> getOrgUnitIdes() {
        return orgUnitIdes;
    }

    public void setOrgUnitIdes(List<Long> orgUnitIdes) {
        this.orgUnitIdes = orgUnitIdes;
    }


    /**
     * Должность сотрудника
    * @return Должность сотрудника
    **/
    


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    /**
     * Роль сотрудника
    * @return Роль сотрудника
    **/
    


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }


    /**
     * Статус корректности мотивации
    * @return Статус корректности мотивации
    **/
    
  @Valid


    public MotivationCorrectStatus getMotivationCorrectStatus() {
        return motivationCorrectStatus;
    }

    public void setMotivationCorrectStatus(MotivationCorrectStatus motivationCorrectStatus) {
        this.motivationCorrectStatus = motivationCorrectStatus;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilterEmployeesRq filterEmployeesRq = (FilterEmployeesRq) o;
        return Objects.equals(this.secondName, filterEmployeesRq.secondName) &&
            Objects.equals(this.firstName, filterEmployeesRq.firstName) &&
            Objects.equals(this.middleName, filterEmployeesRq.middleName) &&
            Objects.equals(this.birthDate, filterEmployeesRq.birthDate) &&
            Objects.equals(this.personnelNumber, filterEmployeesRq.personnelNumber) &&
            Objects.equals(this.mobilePhone, filterEmployeesRq.mobilePhone) &&
            Objects.equals(this.branches, filterEmployeesRq.branches) &&
            Objects.equals(this.orgUnitIdes, filterEmployeesRq.orgUnitIdes) &&
            Objects.equals(this.position, filterEmployeesRq.position) &&
            Objects.equals(this.roleId, filterEmployeesRq.roleId) &&
            Objects.equals(this.motivationCorrectStatus, filterEmployeesRq.motivationCorrectStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secondName, firstName, middleName, birthDate, personnelNumber, mobilePhone, branches, orgUnitIdes, position, roleId, motivationCorrectStatus);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class FilterEmployeesRq {\n");
        
        sb.append("    secondName: ").append(toIndentedString(secondName)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
        sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
        sb.append("    personnelNumber: ").append(toIndentedString(personnelNumber)).append("\n");
        sb.append("    mobilePhone: ").append(toIndentedString(mobilePhone)).append("\n");
        sb.append("    branches: ").append(toIndentedString(branches)).append("\n");
        sb.append("    orgUnitIdes: ").append(toIndentedString(orgUnitIdes)).append("\n");
        sb.append("    position: ").append(toIndentedString(position)).append("\n");
        sb.append("    roleId: ").append(toIndentedString(roleId)).append("\n");
        sb.append("    motivationCorrectStatus: ").append(toIndentedString(motivationCorrectStatus)).append("\n");
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

