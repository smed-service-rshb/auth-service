package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.OrgUnitData;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * CreateSessionRsUser
 */
@Validated
public class CreateSessionRsUser  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("login")
    private String login = null;

    @JsonProperty("secondName")
    private String secondName = null;

    @JsonProperty("firstName")
    private String firstName = null;

    @JsonProperty("middleName")
    private String middleName = null;

    @JsonProperty("mobilePhone")
    private String mobilePhone = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("position")
    private String position = null;

    @JsonProperty("office")
    private String office = null;

    @JsonProperty("branch")
    private String branch = null;

    @JsonProperty("offices")
    @Valid
    private List<OrgUnitData> offices = null;

    @JsonProperty("personnelNumber")
    private String personnelNumber = null;

    @JsonProperty("rights")
    @Valid
    private List<String> rights = null;

    @JsonProperty("changePassword")
    private Boolean changePassword = null;

    @JsonProperty("groupCodes")
    @Valid
    private List<String> groupCodes = null;


    /**
     * Создает пустой экземпляр класса
     */
    public CreateSessionRsUser() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор учётной записи сотрудника
     * @param login Логин сотрудника
     * @param secondName Фамилия сотрудника
     * @param firstName Имя сотрудника
     * @param middleName Отчество сотрудника
     * @param mobilePhone Номер мобильного телефона
     * @param email Адрес электронной почты
     * @param position Должность сотрудника
     * @param office Название ВСП
     * @param branch Название регионального филиала
     * @param offices Список филиалов назначенных пользователю
     * @param personnelNumber Табельный номер
     * @param rights Назначенные права
     * @param changePassword Признак необходимости сменить пароль при входе
     * @param groupCodes Список кодов групп пользователя
     */
    public CreateSessionRsUser(Long id, String login, String secondName, String firstName, String middleName, String mobilePhone, String email, String position, String office, String branch, List<OrgUnitData> offices, String personnelNumber, List<String> rights, Boolean changePassword, List<String> groupCodes) {
        this.id = id;
        this.login = login;
        this.secondName = secondName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.position = position;
        this.office = office;
        this.branch = branch;
        this.offices = offices;
        this.personnelNumber = personnelNumber;
        this.rights = rights;
        this.changePassword = changePassword;
        this.groupCodes = groupCodes;
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
     * Логин сотрудника
    * @return Логин сотрудника
    **/
    


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
     * Номер мобильного телефона
    * @return Номер мобильного телефона
    **/
    


    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }


    /**
     * Адрес электронной почты
    * @return Адрес электронной почты
    **/
    


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
     * Название ВСП
    * @return Название ВСП
    **/
    


    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }


    /**
     * Название регионального филиала
    * @return Название регионального филиала
    **/
    


    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }


    public CreateSessionRsUser addOfficesItem(OrgUnitData officesItem) {
        if (this.offices == null) {
            this.offices = new ArrayList<>();
        }
        this.offices.add(officesItem);
        return this;
    }

    /**
     * Список филиалов назначенных пользователю
    * @return Список филиалов назначенных пользователю
    **/
    
  @Valid


    public List<OrgUnitData> getOffices() {
        return offices;
    }

    public void setOffices(List<OrgUnitData> offices) {
        this.offices = offices;
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


    public CreateSessionRsUser addRightsItem(String rightsItem) {
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


    /**
     * Признак необходимости сменить пароль при входе
    * @return Признак необходимости сменить пароль при входе
    **/
    


    public Boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(Boolean changePassword) {
        this.changePassword = changePassword;
    }


    public CreateSessionRsUser addGroupCodesItem(String groupCodesItem) {
        if (this.groupCodes == null) {
            this.groupCodes = new ArrayList<>();
        }
        this.groupCodes.add(groupCodesItem);
        return this;
    }

    /**
     * Список кодов групп пользователя
    * @return Список кодов групп пользователя
    **/
    


    public List<String> getGroupCodes() {
        return groupCodes;
    }

    public void setGroupCodes(List<String> groupCodes) {
        this.groupCodes = groupCodes;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreateSessionRsUser createSessionRsUser = (CreateSessionRsUser) o;
        return Objects.equals(this.id, createSessionRsUser.id) &&
            Objects.equals(this.login, createSessionRsUser.login) &&
            Objects.equals(this.secondName, createSessionRsUser.secondName) &&
            Objects.equals(this.firstName, createSessionRsUser.firstName) &&
            Objects.equals(this.middleName, createSessionRsUser.middleName) &&
            Objects.equals(this.mobilePhone, createSessionRsUser.mobilePhone) &&
            Objects.equals(this.email, createSessionRsUser.email) &&
            Objects.equals(this.position, createSessionRsUser.position) &&
            Objects.equals(this.office, createSessionRsUser.office) &&
            Objects.equals(this.branch, createSessionRsUser.branch) &&
            Objects.equals(this.offices, createSessionRsUser.offices) &&
            Objects.equals(this.personnelNumber, createSessionRsUser.personnelNumber) &&
            Objects.equals(this.rights, createSessionRsUser.rights) &&
            Objects.equals(this.changePassword, createSessionRsUser.changePassword) &&
            Objects.equals(this.groupCodes, createSessionRsUser.groupCodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, secondName, firstName, middleName, mobilePhone, email, position, office, branch, offices, personnelNumber, rights, changePassword, groupCodes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CreateSessionRsUser {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    login: ").append(toIndentedString(login)).append("\n");
        sb.append("    secondName: ").append(toIndentedString(secondName)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    middleName: ").append(toIndentedString(middleName)).append("\n");
        sb.append("    mobilePhone: ").append(toIndentedString(mobilePhone)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    position: ").append(toIndentedString(position)).append("\n");
        sb.append("    office: ").append(toIndentedString(office)).append("\n");
        sb.append("    branch: ").append(toIndentedString(branch)).append("\n");
        sb.append("    offices: ").append(toIndentedString(offices)).append("\n");
        sb.append("    personnelNumber: ").append(toIndentedString(personnelNumber)).append("\n");
        sb.append("    rights: ").append(toIndentedString(rights)).append("\n");
        sb.append("    changePassword: ").append(toIndentedString(changePassword)).append("\n");
        sb.append("    groupCodes: ").append(toIndentedString(groupCodes)).append("\n");
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

