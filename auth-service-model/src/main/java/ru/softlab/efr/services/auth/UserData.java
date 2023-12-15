package ru.softlab.efr.services.auth;

import ru.softlab.efr.services.auth.exchange.model.MobileLoginRequest;
import ru.softlab.efr.services.auth.exchange.model.OrgUnitData;

import java.io.Serializable;
import java.util.List;

/**
 * Данные по пользователю
 *
 * @author niculichev
 * @since 19.04.2017
 */
public class UserData implements Serializable {
    private static final long serialVersionUID = -3141271584285134943L;

    private Long id;
    private String login;
    private String firstName;
    private String secondName;
    private String middleName;
    private String mobilePhone;
    private String email;
    private String position;
    private String office;
    private String branch;
    private Long orgUnitId;
    private String personnelNumber;
    private Boolean changePassword;
    private List<Right> rights;
    private String password;
    private List<OrgUnitData> offices;
    private List<String> groupCodes;
    private MobileLoginRequest.PlatformEnum platform;
    private String deviceCode;


    public UserData() {
    }

    public UserData(Long id, String login, String firstName, String secondName, String middleName, String mobilePhone,
                    String email, String position, String office, String branch, Long orgUnitId, String personnelNumber,
                    Boolean changePassword, List<Right> rights, List<OrgUnitData> offices, List<String> groupCodes,
                    MobileLoginRequest.PlatformEnum platform, String deviceCode) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.secondName = secondName;
        this.middleName = middleName;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.position = position;
        this.office = office;
        this.branch = branch;
        this.orgUnitId = orgUnitId;
        this.personnelNumber = personnelNumber;
        this.changePassword = changePassword;
        this.rights = rights;
        this.offices = offices;
        this.groupCodes = groupCodes;
        this.platform = platform;
        this.deviceCode = deviceCode;
    }

    /**
     * @return внутренний идентификатор
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return логин
     */
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return имя
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return фамилия
     */
    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    /**
     * @return отчество
     */
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return мобильный телефон
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * @return e-mail
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return должность
     */
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Получить доступные пользователю права
     *
     * @return доступные пользователю права
     */

    public List<Right> getRights() {
        return rights;
    }

    /**
     * Задать доступные пользователю права
     *
     * @param rights доступные пользователю права
     */
    public void setRights(List<Right> rights) {
        this.rights = rights;
    }

    /**
     * @return офис
     */
    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * @return филиал
     */
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * @return табельный номер
     */
    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
    }

    /**
     * @return признак необходимости изменить пароль
     */
    public Boolean getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(Boolean changePassword) {
        this.changePassword = changePassword;
    }

    /**
     * @return уникальный код устройства
     */
    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    /**
     * @return платформа мобильного устройства
     */
    public MobileLoginRequest.PlatformEnum getPlatform() {
        return platform;
    }

    public void setPlatform(MobileLoginRequest.PlatformEnum platform) {
        this.platform = platform;
    }

    public Long getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(Long orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<OrgUnitData> getOffices() {
        return offices;
    }

    public void setOffices(List<OrgUnitData> offices) {
        this.offices = offices;
    }

    public List<String> getGroupCodes() {
        return groupCodes;
    }

    public void setGroupCodes(List<String> groupCodes) {
        this.groupCodes = groupCodes;
    }
}
