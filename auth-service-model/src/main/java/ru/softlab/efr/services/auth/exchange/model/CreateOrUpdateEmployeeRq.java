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
 * CreateOrUpdateEmployeeRq
 */
@Validated
public class CreateOrUpdateEmployeeRq  implements Serializable {
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

    @JsonProperty("password")
    private String password = null;

    @JsonProperty("roles")
    @Valid
    private List<Long> roles = new ArrayList<>();


    /**
     * Создает пустой экземпляр класса
     */
    public CreateOrUpdateEmployeeRq() {}

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
     * @param password пароль
     * @param roles Назначенные роли (идентификаторы)
     */
    public CreateOrUpdateEmployeeRq(String login, String firstName, String secondName, String middleName, String mobilePhone, LocalDate birthDate, String innerPhone, String email, String position, String personnelNumber, List<Long> orgUnits, Long segmentId, List<Long> groupIds, String password, List<Long> roles) {
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
        this.password = password;
        this.roles = roles;
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


    public CreateOrUpdateEmployeeRq addOrgUnitsItem(Long orgUnitsItem) {
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


    public CreateOrUpdateEmployeeRq addGroupIdsItem(Long groupIdsItem) {
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
     * пароль
    * @return пароль
    **/
    


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public CreateOrUpdateEmployeeRq addRolesItem(Long rolesItem) {
        this.roles.add(rolesItem);
        return this;
    }

    /**
     * Назначенные роли (идентификаторы)
    * @return Назначенные роли (идентификаторы)
    **/
      @NotNull



    public List<Long> getRoles() {
        return roles;
    }

    public void setRoles(List<Long> roles) {
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
        CreateOrUpdateEmployeeRq createOrUpdateEmployeeRq = (CreateOrUpdateEmployeeRq) o;
        return Objects.equals(this.login, createOrUpdateEmployeeRq.login) &&
            Objects.equals(this.firstName, createOrUpdateEmployeeRq.firstName) &&
            Objects.equals(this.secondName, createOrUpdateEmployeeRq.secondName) &&
            Objects.equals(this.middleName, createOrUpdateEmployeeRq.middleName) &&
            Objects.equals(this.mobilePhone, createOrUpdateEmployeeRq.mobilePhone) &&
            Objects.equals(this.birthDate, createOrUpdateEmployeeRq.birthDate) &&
            Objects.equals(this.innerPhone, createOrUpdateEmployeeRq.innerPhone) &&
            Objects.equals(this.email, createOrUpdateEmployeeRq.email) &&
            Objects.equals(this.position, createOrUpdateEmployeeRq.position) &&
            Objects.equals(this.personnelNumber, createOrUpdateEmployeeRq.personnelNumber) &&
            Objects.equals(this.orgUnits, createOrUpdateEmployeeRq.orgUnits) &&
            Objects.equals(this.segmentId, createOrUpdateEmployeeRq.segmentId) &&
            Objects.equals(this.groupIds, createOrUpdateEmployeeRq.groupIds) &&
            Objects.equals(this.password, createOrUpdateEmployeeRq.password) &&
            Objects.equals(this.roles, createOrUpdateEmployeeRq.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, firstName, secondName, middleName, mobilePhone, birthDate, innerPhone, email, position, personnelNumber, orgUnits, segmentId, groupIds, password, roles);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CreateOrUpdateEmployeeRq {\n");
        
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
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
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

