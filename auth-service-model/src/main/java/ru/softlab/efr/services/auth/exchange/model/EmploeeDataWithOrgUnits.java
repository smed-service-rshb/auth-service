package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.OrgUnitData;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * EmploeeDataWithOrgUnits
 */
@Validated
public class EmploeeDataWithOrgUnits  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id = null;

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
    private List<OrgUnitData> orgUnits = null;

    @JsonProperty("segmentId")
    private Long segmentId = null;

    @JsonProperty("groupIds")
    @Valid
    private List<Long> groupIds = null;


    /**
     * Создает пустой экземпляр класса
     */
    public EmploeeDataWithOrgUnits() {}

    /**
     * Создает экземпляр класса
     * @param id id
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
     * @param orgUnits Список орг. структур
     * @param segmentId Идентификатор сегмента
     * @param groupIds Список идентификаторов групп пользователя
     */
    public EmploeeDataWithOrgUnits(Long id, String login, String firstName, String secondName, String middleName, String mobilePhone, LocalDate birthDate, String innerPhone, String email, String position, String personnelNumber, List<OrgUnitData> orgUnits, Long segmentId, List<Long> groupIds) {
        this.id = id;
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
    }

    /**
     * id
    * @return id
    **/
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    


    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
    }


    public EmploeeDataWithOrgUnits addOrgUnitsItem(OrgUnitData orgUnitsItem) {
        if (this.orgUnits == null) {
            this.orgUnits = new ArrayList<>();
        }
        this.orgUnits.add(orgUnitsItem);
        return this;
    }

    /**
     * Список орг. структур
    * @return Список орг. структур
    **/
    
  @Valid


    public List<OrgUnitData> getOrgUnits() {
        return orgUnits;
    }

    public void setOrgUnits(List<OrgUnitData> orgUnits) {
        this.orgUnits = orgUnits;
    }


    /**
     * Идентификатор сегмента
    * @return Идентификатор сегмента
    **/
    


    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }


    public EmploeeDataWithOrgUnits addGroupIdsItem(Long groupIdsItem) {
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


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmploeeDataWithOrgUnits emploeeDataWithOrgUnits = (EmploeeDataWithOrgUnits) o;
        return Objects.equals(this.id, emploeeDataWithOrgUnits.id) &&
            Objects.equals(this.login, emploeeDataWithOrgUnits.login) &&
            Objects.equals(this.firstName, emploeeDataWithOrgUnits.firstName) &&
            Objects.equals(this.secondName, emploeeDataWithOrgUnits.secondName) &&
            Objects.equals(this.middleName, emploeeDataWithOrgUnits.middleName) &&
            Objects.equals(this.mobilePhone, emploeeDataWithOrgUnits.mobilePhone) &&
            Objects.equals(this.birthDate, emploeeDataWithOrgUnits.birthDate) &&
            Objects.equals(this.innerPhone, emploeeDataWithOrgUnits.innerPhone) &&
            Objects.equals(this.email, emploeeDataWithOrgUnits.email) &&
            Objects.equals(this.position, emploeeDataWithOrgUnits.position) &&
            Objects.equals(this.personnelNumber, emploeeDataWithOrgUnits.personnelNumber) &&
            Objects.equals(this.orgUnits, emploeeDataWithOrgUnits.orgUnits) &&
            Objects.equals(this.segmentId, emploeeDataWithOrgUnits.segmentId) &&
            Objects.equals(this.groupIds, emploeeDataWithOrgUnits.groupIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, firstName, secondName, middleName, mobilePhone, birthDate, innerPhone, email, position, personnelNumber, orgUnits, segmentId, groupIds);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EmploeeDataWithOrgUnits {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

