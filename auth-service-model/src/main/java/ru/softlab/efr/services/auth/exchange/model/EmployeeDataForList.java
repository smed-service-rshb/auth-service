package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.EmployeeData;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * EmployeeDataForList
 */
@Validated
public class EmployeeDataForList  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("login")
    private String login = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("secondName")
    private String secondName = null;

    @JsonProperty("middleName")
    private String middleName = null;

    @JsonProperty("mobilePhone")
    private String mobilePhone = null;

    @JsonProperty("birthDate")
    private LocalDate birthDate = null;

    @JsonProperty("innerPhone")
    private String innerPhone = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("position")
    private String position = null;

    @JsonProperty("personnelNumber")
    private String personnelNumber = null;

    @JsonProperty("orgUnits")
    @Valid
    private List<Long> orgUnits = null;

    @JsonProperty("segmentId")
    private Long segmentId = null;

    @JsonProperty("groupIds")
    @Valid
    private List<Long> groupIds = null;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("fullName")
    private String fullName = null;

    @JsonProperty("branches")
    @Valid
    private List<String> branches = null;

    @JsonProperty("offices")
    @Valid
    private List<String> offices = null;

    @JsonProperty("segment")
    private String segment = null;

    @JsonProperty("roles")
    @Valid
    private List<String> roles = null;

    @JsonProperty("lockStatus")
    private Boolean lockStatus = null;


    /**
     * Создает пустой экземпляр класса
     */
    public EmployeeDataForList() {}

    /**
     * Создает экземпляр класса
     * @param login логин
     * @param firstName имя
     * @param secondName фамилия
     * @param middleName отчество
     * @param mobilePhone мобильный телефон
     * @param birthDate Дата рождения
     * @param innerPhone внутренний телефон
     * @param email адрес электронной почты
     * @param position должность
     * @param personnelNumber табельный номер
     * @param orgUnits Список идентификаторов орг. структур
     * @param segmentId Идентификатор сегмента
     * @param groupIds Список идентификаторов групп пользователя
     * @param id Идентификатор учётной записи сотрудника
     * @param fullName ФИО сотрудника
     * @param branches Список наименований региональных филиалов
     * @param offices Список наименований офисов
     * @param segment Название сегмента
     * @param roles Назначенные роли
     * @param lockStatus Признак, блокировки пользователя в системе
     */
    public EmployeeDataForList(String login, String firstName, String secondName, String middleName, String mobilePhone, LocalDate birthDate, String innerPhone, String email, String position, String personnelNumber, List<Long> orgUnits, Long segmentId, List<Long> groupIds, Long id, String fullName, List<String> branches, List<String> offices, String segment, List<String> roles, Boolean lockStatus) {
        this.login = login;
        this.firstName = firstName;
        this.secondName = secondName;
        this.middleName = middleName;
        this.mobilePhone = mobilePhone;
        this.birthDate = birthDate;
        this.innerPhone = innerPhone;
        this.email = email;
        this.position = position;
        this.personnelNumber = personnelNumber;
        this.orgUnits = orgUnits;
        this.segmentId = segmentId;
        this.groupIds = groupIds;
        this.id = id;
        this.fullName = fullName;
        this.branches = branches;
        this.offices = offices;
        this.segment = segment;
        this.roles = roles;
        this.lockStatus = lockStatus;
    }

    /**
     * логин
    * @return логин
    **/
    


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    /**
     * имя
    * @return имя
    **/
      @NotNull

@Size(min=1,max=255) 

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    /**
     * фамилия
    * @return фамилия
    **/
      @NotNull

@Size(min=1,max=255) 

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }


    /**
     * отчество
    * @return отчество
    **/
    
@Size(min=1,max=255) 

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    /**
     * мобильный телефон
    * @return мобильный телефон
    **/
    
@Size(max=20) 

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
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
     * внутренний телефон
    * @return внутренний телефон
    **/
    
@Size(max=20) 

    public String getInnerPhone() {
        return innerPhone;
    }

    public void setInnerPhone(String innerPhone) {
        this.innerPhone = innerPhone;
    }


    /**
     * адрес электронной почты
    * @return адрес электронной почты
    **/
    
@Pattern(regexp="(^(((\\w+-)|(\\w+\\.))*\\w+@(\\w+\\.)+[a-zA-Z]{2,6})$)") @Size(max=150) 

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * должность
    * @return должность
    **/
    
@Size(max=50) 

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    /**
     * табельный номер
    * @return табельный номер
    **/
      @NotNull



    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
    }


    public EmployeeDataForList addOrgUnitsItem(Long orgUnitsItem) {
        if (this.orgUnits == null) {
            this.orgUnits = new ArrayList<>();
        }
        this.orgUnits.add(orgUnitsItem);
        return this;
    }

    /**
     * Список идентификаторов орг. структур
    * @return Список идентификаторов орг. структур
    **/
    


    public List<Long> getOrgUnits() {
        return orgUnits;
    }

    public void setOrgUnits(List<Long> orgUnits) {
        this.orgUnits = orgUnits;
    }


    /**
     * Идентификатор сегмента
    * @return Идентификатор сегмента
    **/
      @NotNull



    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }


    public EmployeeDataForList addGroupIdsItem(Long groupIdsItem) {
        if (this.groupIds == null) {
            this.groupIds = new ArrayList<>();
        }
        this.groupIds.add(groupIdsItem);
        return this;
    }

    /**
     * Список идентификаторов групп пользователя
    * @return Список идентификаторов групп пользователя
    **/
    


    public List<Long> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Long> groupIds) {
        this.groupIds = groupIds;
    }


    /**
     * Идентификатор учётной записи сотрудника
    * @return Идентификатор учётной записи сотрудника
    **/
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * ФИО сотрудника
    * @return ФИО сотрудника
    **/
    


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public EmployeeDataForList addBranchesItem(String branchesItem) {
        if (this.branches == null) {
            this.branches = new ArrayList<>();
        }
        this.branches.add(branchesItem);
        return this;
    }

    /**
     * Список наименований региональных филиалов
    * @return Список наименований региональных филиалов
    **/
    


    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> branches) {
        this.branches = branches;
    }


    public EmployeeDataForList addOfficesItem(String officesItem) {
        if (this.offices == null) {
            this.offices = new ArrayList<>();
        }
        this.offices.add(officesItem);
        return this;
    }

    /**
     * Список наименований офисов
    * @return Список наименований офисов
    **/
    


    public List<String> getOffices() {
        return offices;
    }

    public void setOffices(List<String> offices) {
        this.offices = offices;
    }


    /**
     * Название сегмента
    * @return Название сегмента
    **/
    


    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }


    public EmployeeDataForList addRolesItem(String rolesItem) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(rolesItem);
        return this;
    }

    /**
     * Назначенные роли
    * @return Назначенные роли
    **/
    


    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }


    /**
     * Признак, блокировки пользователя в системе
    * @return Признак, блокировки пользователя в системе
    **/
    


    public Boolean isLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(Boolean lockStatus) {
        this.lockStatus = lockStatus;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmployeeDataForList employeeDataForList = (EmployeeDataForList) o;
        return Objects.equals(this.login, employeeDataForList.login) &&
            Objects.equals(this.firstName, employeeDataForList.firstName) &&
            Objects.equals(this.secondName, employeeDataForList.secondName) &&
            Objects.equals(this.middleName, employeeDataForList.middleName) &&
            Objects.equals(this.mobilePhone, employeeDataForList.mobilePhone) &&
            Objects.equals(this.birthDate, employeeDataForList.birthDate) &&
            Objects.equals(this.innerPhone, employeeDataForList.innerPhone) &&
            Objects.equals(this.email, employeeDataForList.email) &&
            Objects.equals(this.position, employeeDataForList.position) &&
            Objects.equals(this.personnelNumber, employeeDataForList.personnelNumber) &&
            Objects.equals(this.orgUnits, employeeDataForList.orgUnits) &&
            Objects.equals(this.segmentId, employeeDataForList.segmentId) &&
            Objects.equals(this.groupIds, employeeDataForList.groupIds) &&
            Objects.equals(this.id, employeeDataForList.id) &&
            Objects.equals(this.fullName, employeeDataForList.fullName) &&
            Objects.equals(this.branches, employeeDataForList.branches) &&
            Objects.equals(this.offices, employeeDataForList.offices) &&
            Objects.equals(this.segment, employeeDataForList.segment) &&
            Objects.equals(this.roles, employeeDataForList.roles) &&
            Objects.equals(this.lockStatus, employeeDataForList.lockStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, firstName, secondName, middleName, mobilePhone, birthDate, innerPhone, email, position, personnelNumber, orgUnits, segmentId, groupIds, id, fullName, branches, offices, segment, roles, lockStatus);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EmployeeDataForList {\n");
        
        sb.append("    login: ").append(toIndentedString(login)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    secondName: ").append(toIndentedString(secondName)).append("\n");
        sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
        sb.append("    mobilePhone: ").append(toIndentedString(mobilePhone)).append("\n");
        sb.append("    birthDate: ").append(toIndentedString(birthDate)).append("\n");
        sb.append("    innerPhone: ").append(toIndentedString(innerPhone)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    position: ").append(toIndentedString(position)).append("\n");
        sb.append("    personnelNumber: ").append(toIndentedString(personnelNumber)).append("\n");
        sb.append("    orgUnits: ").append(toIndentedString(orgUnits)).append("\n");
        sb.append("    segmentId: ").append(toIndentedString(segmentId)).append("\n");
        sb.append("    groupIds: ").append(toIndentedString(groupIds)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
        sb.append("    branches: ").append(toIndentedString(branches)).append("\n");
        sb.append("    offices: ").append(toIndentedString(offices)).append("\n");
        sb.append("    segment: ").append(toIndentedString(segment)).append("\n");
        sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
        sb.append("    lockStatus: ").append(toIndentedString(lockStatus)).append("\n");
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

