package ru.softlab.efr.services.auth.model;

import java.util.Objects;

/**
 * Внутренняя модель элемента отчета для мотивации
 */
public class MotivationReportModel {

    private String employerSNM;
    private String employerPersonnelNumber;
    private String position;
    private String office;
    private String orgUnit;
    private String employerRole;
    private String employerSegment;
    private String accountNumber;
    private String bankBIK;
    private String bankName;
    private String inn;
    private String registrationAddress;
    private String registrationIndex;
    private String latestAgreementUpdate;
    private String latestPassportUpdate;
    private String correctness;
    private String comment;
    private String motivationStartDate;


    /**
     * Создает пустой экземпляр класса
     */
    public MotivationReportModel() {
    }

    /**
     * Создает экземпляр класса
     *
     * @param employerSNM             ФИО сотрудника
     * @param employerPersonnelNumber табельный номер сотрудника
     * @param position                Должность сотнудника
     * @param office                  ВСП сотрудника
     * @param orgUnit                 РФ сотрудника
     * @param employerRole            роль сотрудника
     * @param employerSegment         сегмент сотрудника
     * @param accountNumber           номер счета РСХБ
     * @param bankBIK                 БИК банка
     * @param bankName                имя банка
     * @param inn                     ИНН
     * @param registrationAddress     адрес регистрации
     * @param registrationIndex       индекас регистрации
     * @param latestAgreementUpdate   последние обновление документа согласия
     * @param latestPassportUpdate    последние обновление паспорта
     * @param correctness             корректность документов
     * @param comment                 комментарий
     * @param motivationStartDate     дата начала действия мотивации
     */
    public MotivationReportModel(String employerSNM, String employerPersonnelNumber, String position, String office, String orgUnit, String employerRole, String employerSegment, String accountNumber, String bankBIK, String bankName, String inn, String registrationAddress, String registrationIndex, String latestAgreementUpdate, String latestPassportUpdate, String correctness, String comment, String motivationStartDate) {
        this.employerSNM = employerSNM;
        this.employerPersonnelNumber = employerPersonnelNumber;
        this.position = position;
        this.office = office;
        this.orgUnit = orgUnit;
        this.employerRole = employerRole;
        this.employerSegment = employerSegment;
        this.accountNumber = accountNumber;
        this.bankBIK = bankBIK;
        this.bankName = bankName;
        this.inn = inn;
        this.registrationAddress = registrationAddress;
        this.registrationIndex = registrationIndex;
        this.latestAgreementUpdate = latestAgreementUpdate;
        this.latestPassportUpdate = latestPassportUpdate;
        this.correctness = correctness;
        this.comment = comment;
        this.motivationStartDate = motivationStartDate;
    }

    /**
     * ФИО сотрудника
     *
     * @return ФИО сотрудника
     **/


    public String getEmployerSNM() {
        return employerSNM;
    }

    public void setEmployerSNM(String employerSNM) {
        this.employerSNM = employerSNM;
    }


    /**
     * табельный номер сотрудника
     *
     * @return табельный номер сотрудника
     **/


    public String getEmployerPersonnelNumber() {
        return employerPersonnelNumber;
    }

    public void setEmployerPersonnelNumber(String employerPersonnelNumber) {
        this.employerPersonnelNumber = employerPersonnelNumber;
    }


    /**
     * Должность сотнудника
     *
     * @return Должность сотнудника
     **/


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    /**
     * ВСП сотрудника
     *
     * @return ВСП сотрудника
     **/


    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }


    /**
     * РФ сотрудника
     *
     * @return РФ сотрудника
     **/


    public String getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }


    /**
     * роль сотрудника
     *
     * @return роль сотрудника
     **/


    public String getEmployerRole() {
        return employerRole;
    }

    public void setEmployerRole(String employerRole) {
        this.employerRole = employerRole;
    }


    /**
     * сегмент сотрудника
     *
     * @return сегмент сотрудника
     **/


    public String getEmployerSegment() {
        return employerSegment;
    }

    public void setEmployerSegment(String employerSegment) {
        this.employerSegment = employerSegment;
    }


    /**
     * номер счета РСХБ
     *
     * @return номер счета РСХБ
     **/


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    /**
     * БИК банка
     *
     * @return БИК банка
     **/


    public String getBankBIK() {
        return bankBIK;
    }

    public void setBankBIK(String bankBIK) {
        this.bankBIK = bankBIK;
    }


    /**
     * имя банка
     *
     * @return имя банка
     **/


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    /**
     * ИНН
     *
     * @return ИНН
     **/


    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }


    /**
     * адрес регистрации
     *
     * @return адрес регистрации
     **/


    public String getRegistrationAddress() {
        return registrationAddress;
    }

    public void setRegistrationAddress(String registrationAddress) {
        this.registrationAddress = registrationAddress;
    }


    /**
     * индекас регистрации
     *
     * @return индекас регистрации
     **/


    public String getRegistrationIndex() {
        return registrationIndex;
    }

    public void setRegistrationIndex(String registrationIndex) {
        this.registrationIndex = registrationIndex;
    }


    /**
     * последние обновление документа
     *
     * @return последние обновление документа согласия
     **/


    public String getLatestAgreementUpdate() {
        return latestAgreementUpdate;
    }

    public void setLatestAgreementUpdate(String latestAgreementUpdate) {
        this.latestAgreementUpdate = latestAgreementUpdate;
    }

    /**
     * последние обновление документа
     *
     * @return последние обновление паспорта
     **/


    public String getLatestPassportUpdate() {
        return latestPassportUpdate;
    }

    public void setLatestPassportUpdate(String latestPassportUpdate) {
        this.latestPassportUpdate = latestPassportUpdate;
    }


    /**
     * корректность документов
     *
     * @return корректность документов
     **/


    public String getCorrectness() {
        return correctness;
    }

    public void setCorrectness(String correctness) {
        this.correctness = correctness;
    }


    /**
     * комментарий
     *
     * @return комментарий
     **/


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    /**
     * дата начала действия мотивации
     *
     * @return дата начала действия мотивации
     **/


    public String getMotivationStartDate() {
        return motivationStartDate;
    }

    public void setMotivationStartDate(String motivationStartDate) {
        this.motivationStartDate = motivationStartDate;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MotivationReportModel motivationReportModel = (MotivationReportModel) o;
        return Objects.equals(this.employerSNM, motivationReportModel.employerSNM) &&
                Objects.equals(this.employerPersonnelNumber, motivationReportModel.employerPersonnelNumber) &&
                Objects.equals(this.position, motivationReportModel.position) &&
                Objects.equals(this.office, motivationReportModel.office) &&
                Objects.equals(this.orgUnit, motivationReportModel.orgUnit) &&
                Objects.equals(this.employerRole, motivationReportModel.employerRole) &&
                Objects.equals(this.employerSegment, motivationReportModel.employerSegment) &&
                Objects.equals(this.accountNumber, motivationReportModel.accountNumber) &&
                Objects.equals(this.bankBIK, motivationReportModel.bankBIK) &&
                Objects.equals(this.bankName, motivationReportModel.bankName) &&
                Objects.equals(this.inn, motivationReportModel.inn) &&
                Objects.equals(this.registrationAddress, motivationReportModel.registrationAddress) &&
                Objects.equals(this.registrationIndex, motivationReportModel.registrationIndex) &&
                Objects.equals(this.latestAgreementUpdate, motivationReportModel.latestAgreementUpdate) &&
                Objects.equals(this.latestPassportUpdate, motivationReportModel.latestPassportUpdate) &&
                Objects.equals(this.correctness, motivationReportModel.correctness) &&
                Objects.equals(this.comment, motivationReportModel.comment) &&
                Objects.equals(this.motivationStartDate, motivationReportModel.motivationStartDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employerSNM, employerPersonnelNumber, position, office, orgUnit, employerRole, employerSegment, accountNumber, bankBIK, bankName, inn, registrationAddress, registrationIndex, latestAgreementUpdate, latestPassportUpdate, correctness, comment, motivationStartDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MotivationReportModel {\n");

        sb.append("    employerSNM: ").append(toIndentedString(employerSNM)).append("\n");
        sb.append("    employerPersonnelNumber: ").append(toIndentedString(employerPersonnelNumber)).append("\n");
        sb.append("    position: ").append(toIndentedString(position)).append("\n");
        sb.append("    office: ").append(toIndentedString(office)).append("\n");
        sb.append("    orgUnit: ").append(toIndentedString(orgUnit)).append("\n");
        sb.append("    employerRole: ").append(toIndentedString(employerRole)).append("\n");
        sb.append("    employerSegment: ").append(toIndentedString(employerSegment)).append("\n");
        sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
        sb.append("    bankBIK: ").append(toIndentedString(bankBIK)).append("\n");
        sb.append("    bankName: ").append(toIndentedString(bankName)).append("\n");
        sb.append("    inn: ").append(toIndentedString(inn)).append("\n");
        sb.append("    registrationAddress: ").append(toIndentedString(registrationAddress)).append("\n");
        sb.append("    registrationIndex: ").append(toIndentedString(registrationIndex)).append("\n");
        sb.append("    latestUpdate: ").append(toIndentedString(latestAgreementUpdate)).append("\n");
        sb.append("    latestPassportUpdate: ").append(toIndentedString(latestPassportUpdate)).append("\n");
        sb.append("    correctness: ").append(toIndentedString(correctness)).append("\n");
        sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
        sb.append("    motivationStartDate: ").append(toIndentedString(motivationStartDate)).append("\n");
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

