package ru.softlab.efr.services.authorization;

import ru.softlab.efr.services.auth.Right;

import java.util.List;

/**
 * Данные по текущему(в разрезе нитки) принципалу
 *
 * @author niculichev
 * @since 30.05.2017
 */
public class PrincipalDataImpl implements PrincipalData {
    private Long id;
    private String login;
    private String firstName;
    private String secondName;
    private String middleName;
    private List<Right> rights;
    private String office;
    private Long officeId;
    private String branch;
    private String mobilePhone;
    private String personnelNumber;
    private List<String> offices;
    private List<String> groups;

    /**
     * @return внутренний идентификатор
     */
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return логин пользователя
     */
    @Override
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return имя
     */
    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return фамилия
     */
    @Override
    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    /**
     * @return отчество
     */
    @Override
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return присвоенные права
     */
    @Override
    public List<Right> getRights() {
        return rights;
    }

    public void setRights(List<Right> rights) {
        this.rights = rights;
    }

    /**
     * @return офис
     */
    @Override
    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * @return идентификатор офиса
     */
    @Override
    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    /**
     * @return филиал
     */
    @Override
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * @return номер мобильного телефона
     */
    @Override
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * @return табельный номер
     */
    @Override
    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
    }

    /**
     * @return список наименований офисов закрепленных за пользователем
     */
    @Override
    public List<String> getOffices() {
        return offices;
    }

    public void setOffices(List<String> offices) {
        this.offices = offices;
    }

    @Override
    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
