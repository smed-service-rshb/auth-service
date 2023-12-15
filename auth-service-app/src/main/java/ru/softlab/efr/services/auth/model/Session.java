package ru.softlab.efr.services.auth.model;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сущность, ассоциированная с БД таблицей "sessions", которая хранит сессии сотрудников
 *
 * @author niculichev
 * @since 20.04.2017
 */
@Entity
@Table(name = "sessions")
public class Session {
    private static final char RIGHT_SEPARATOR_CHAR = ',';

    @Id
    private String uid = UUID.randomUUID().toString();
    private Date creationDate;
    private State state;

    private Long ownerId;
    private String ownerLogin;
    private String ownerFirstName;
    private String ownerSecondName;
    private String ownerMiddleName;
    private String ownerMobilePhone;
    private String ownerEmail;
    private String ownerPosition;
    private String ownerOffice;
    private String ownerBranch;
    private String ownerPersonnelNumber;
    @Column(name = "ownerRights")
    @Access(AccessType.FIELD)
    private String ownerRights;
    private Boolean changePassword;

    /**
     * Получить идентификатор
     *
     * @return идентификатор
     */
    public String getUid() {
        return uid;
    }

    /**
     * Задать идентификатор
     *
     * @param uid идентификатор
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Получить дату создания
     *
     * @return дату создания
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Задать дату создания
     *
     * @param creationDate дату создания
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Получить состояние
     *
     * @return состояние
     */
    public State getState() {
        return state;
    }

    /**
     * Задать состояние
     *
     * @param state состояние
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Получить идентификатор учётной записи владельца.
     *
     * @return Идентификатор учётной записи владельца.
     */
    public Long getOwnerId() {
        return ownerId;
    }

    /**
     * Задать идентификатор учётной записи владельца.
     * @param ownerId Идентификатор владельца
     */
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Получить логин владельца
     *
     * @return логин владельца
     */
    public String getOwnerLogin() {
        return ownerLogin;
    }

    /**
     * Задать лоигн владельца
     *
     * @param ownerLogin лоигн владельца
     */
    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    /**
     * Получить имя владельца
     *
     * @return имя владельца
     */
    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    /**
     * Задать имя владельца
     *
     * @param ownerFirstName имя владельца
     */
    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    /**
     * Получить фамилию владельца
     *
     * @return фамилия владельца
     */
    public String getOwnerSecondName() {
        return ownerSecondName;
    }

    /**
     * Задать фамилию владельца
     *
     * @param ownerSecondName фамилия владельца
     */
    public void setOwnerSecondName(String ownerSecondName) {
        this.ownerSecondName = ownerSecondName;
    }

    /**
     * Получить отчество владельца
     *
     * @return отчество владельца
     */
    public String getOwnerMiddleName() {
        return ownerMiddleName;
    }

    /**
     * Задать отчество владельца
     *
     * @param ownerMiddleName отчество владельца
     */
    public void setOwnerMiddleName(String ownerMiddleName) {
        this.ownerMiddleName = ownerMiddleName;
    }

    /**
     * Получить номер мобильного телефона владельца
     *
     * @return номер мобильного телефона владельца
     */
    public String getOwnerMobilePhone() {
        return ownerMobilePhone;
    }

    /**
     * Задать номер мобильного телефона владельца
     *
     * @param ownerMobilePhone номер мобильного телефона владельца
     */
    public void setOwnerMobilePhone(String ownerMobilePhone) {
        this.ownerMobilePhone = ownerMobilePhone;
    }

    /**
     * Получить адрес электронной почны владельца
     *
     * @return адрес электронной почны владельца
     */
    public String getOwnerEmail() {
        return ownerEmail;
    }

    /**
     * Задать адрес электронной почны владельца
     *
     * @param ownerEmail адрес электронной почны владельца
     */
    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    /**
     * Получить должность владельца
     *
     * @return должность владельца
     */
    public String getOwnerPosition() {
        return ownerPosition;
    }

    /**
     * Задать должность владельца
     *
     * @param ownerPosition должность владельца
     */
    public void setOwnerPosition(String ownerPosition) {
        this.ownerPosition = ownerPosition;
    }

    /**
     * Получить офис владельца
     *
     * @return офис владельца
     */
    public String getOwnerOffice() {
        return ownerOffice;
    }

    /**
     * Задать офис владельца
     *
     * @param ownerOffice офис владельца
     */
    public void setOwnerOffice(String ownerOffice) {
        this.ownerOffice = ownerOffice;
    }

    /**
     * Получить подразделение владельца
     *
     * @return подразделение владельца
     */
    public String getOwnerBranch() {
        return ownerBranch;
    }

    /**
     * Задать подразделение владельца
     *
     * @param ownerBranch подразделение владельца
     */
    public void setOwnerBranch(String ownerBranch) {
        this.ownerBranch = ownerBranch;
    }

    /**
     * Получить персональный номер владельца
     *
     * @return персональный номер владельца
     */
    public String getOwnerPersonnelNumber() {
        return ownerPersonnelNumber;
    }

    /**
     * Задать персональный номер владельца
     *
     * @param ownerPersonnelNumber персональный номер владельца
     */
    public void setOwnerPersonnelNumber(String ownerPersonnelNumber) {
        this.ownerPersonnelNumber = ownerPersonnelNumber;
    }

    /**
     * @return получить признак необходимости смены пароля
     */
    public Boolean getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(Boolean changePassword) {
        this.changePassword = changePassword;
    }

    /**
     * Получить роли владельца
     *
     * @return роли владельца
     */
    public List<ru.softlab.efr.services.auth.Right> getOwnerRights() {
        if (ownerRights == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(StringUtils.split(ownerRights, RIGHT_SEPARATOR_CHAR)).map(ru.softlab.efr.services.auth.Right::valueOf).collect(Collectors.toList());
    }

    /**
     * Задать роли владельца
     *
     * @param ownerRights роли владельца
     */
    public void setOwnerRights(List<ru.softlab.efr.services.auth.Right> ownerRights) {
        this.ownerRights = StringUtils.join(ownerRights, RIGHT_SEPARATOR_CHAR);
    }

    /**
     * Состояние сессии
     */
    public enum State {

        /**
         * Активная
         */
        active,

        /**
         * Закрытая
         */
        closed
    }
}
