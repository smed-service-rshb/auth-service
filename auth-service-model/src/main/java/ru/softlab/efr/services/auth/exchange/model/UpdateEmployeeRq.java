package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.EmployeeData;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UpdateEmployeeRq
 */
@Validated
public class UpdateEmployeeRq   {
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

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("position")
    private String position = null;

    @JsonProperty("personnelNumber")
    private String personnelNumber = null;

    @JsonProperty("orgUnitId")
    private Long orgUnitId = null;

    @JsonProperty("segmentId")
    private Long segmentId = null;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("password")
    private String password = null;

    @JsonProperty("roles")
    @Valid
    private List<Long> roles = new ArrayList<Long>();


    /**
     * Создает пустой экземпляр класса
     */
    public UpdateEmployeeRq() {}

    /**
     * Создает экземпляр класса
     * @param login логин
     * @param firstName имя
     * @param secondName фамилия
     * @param middleName отчество
     * @param mobilePhone мобильный телефон
     * @param email адрес электронной почты
     * @param position должность
     * @param personnelNumber табельный номер
     * @param orgUnitId Идентификатор объекта орг. структуры
     * @param segmentId Идентификатор сегмента
     * @param id идентификатор сотрудника
     * @param password пароль
     * @param roles Назначенные роли (идентификаторы)
     */
    public UpdateEmployeeRq(String login, String firstName, String secondName, String middleName, String mobilePhone, String email, String position, String personnelNumber, Long orgUnitId, Long segmentId, Long id, String password, List<Long> roles) {
        this.login = login;
        this.firstName = firstName;
        this.secondName = secondName;
        this.middleName = middleName;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.position = position;
        this.personnelNumber = personnelNumber;
        this.orgUnitId = orgUnitId;
        this.segmentId = segmentId;
        this.id = id;
        this.password = password;
        this.roles = roles;
    }

    /**
     * логин
    * @return логин
    **/
    @ApiModelProperty(value = "логин")
    


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
    @ApiModelProperty(required = true, value = "имя")
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
    @ApiModelProperty(required = true, value = "фамилия")
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
    @ApiModelProperty(required = true, value = "отчество")
      @NotNull

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
    @ApiModelProperty(required = true, value = "мобильный телефон")
      @NotNull

 @Size(max=20)

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }


    /**
     * адрес электронной почты
    * @return адрес электронной почты
    **/
    @ApiModelProperty(value = "адрес электронной почты")
    
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
    @ApiModelProperty(value = "должность")
    
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
    @ApiModelProperty(value = "табельный номер")
    


    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
    }


    /**
     * Идентификатор объекта орг. структуры
    * @return Идентификатор объекта орг. структуры
    **/
    @ApiModelProperty(value = "Идентификатор объекта орг. структуры")
    


    public Long getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(Long orgUnitId) {
        this.orgUnitId = orgUnitId;
    }


    /**
     * Идентификатор сегмента
    * @return Идентификатор сегмента
    **/
    @ApiModelProperty(value = "Идентификатор сегмента")
    


    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }


    /**
     * идентификатор сотрудника
    * @return идентификатор сотрудника
    **/
    @ApiModelProperty(value = "идентификатор сотрудника")
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * пароль
    * @return пароль
    **/
    @ApiModelProperty(value = "пароль")
    


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public UpdateEmployeeRq addRolesItem(Long rolesItem) {
        this.roles.add(rolesItem);
        return this;
    }

    /**
     * Назначенные роли (идентификаторы)
    * @return Назначенные роли (идентификаторы)
    **/
    @ApiModelProperty(required = true, value = "Назначенные роли (идентификаторы)")
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
        UpdateEmployeeRq updateEmployeeRq = (UpdateEmployeeRq) o;
        return Objects.equals(this.login, updateEmployeeRq.login) &&
            Objects.equals(this.firstName, updateEmployeeRq.firstName) &&
            Objects.equals(this.secondName, updateEmployeeRq.secondName) &&
            Objects.equals(this.middleName, updateEmployeeRq.middleName) &&
            Objects.equals(this.mobilePhone, updateEmployeeRq.mobilePhone) &&
            Objects.equals(this.email, updateEmployeeRq.email) &&
            Objects.equals(this.position, updateEmployeeRq.position) &&
            Objects.equals(this.personnelNumber, updateEmployeeRq.personnelNumber) &&
            Objects.equals(this.orgUnitId, updateEmployeeRq.orgUnitId) &&
            Objects.equals(this.segmentId, updateEmployeeRq.segmentId) &&
            Objects.equals(this.id, updateEmployeeRq.id) &&
            Objects.equals(this.password, updateEmployeeRq.password) &&
            Objects.equals(this.roles, updateEmployeeRq.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, firstName, secondName, middleName, mobilePhone, email, position, personnelNumber, orgUnitId, segmentId, id, password, roles);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UpdateEmployeeRq {\n");
        
        sb.append("    login: ").append(toIndentedString(login)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    secondName: ").append(toIndentedString(secondName)).append("\n");
        sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
        sb.append("    mobilePhone: ").append(toIndentedString(mobilePhone)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    position: ").append(toIndentedString(position)).append("\n");
        sb.append("    personnelNumber: ").append(toIndentedString(personnelNumber)).append("\n");
        sb.append("    orgUnitId: ").append(toIndentedString(orgUnitId)).append("\n");
        sb.append("    segmentId: ").append(toIndentedString(segmentId)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

