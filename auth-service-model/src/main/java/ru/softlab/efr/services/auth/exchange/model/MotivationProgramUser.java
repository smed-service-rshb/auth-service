package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import ru.softlab.efr.services.auth.exchange.model.MotivationCorrectStatus;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Программа мотивации
 */
@Validated
public class MotivationProgramUser  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("accountNumber")
    private String accountNumber = null;

    @JsonProperty("bikCode")
    private String bikCode = null;

    @JsonProperty("bankName")
    private String bankName = null;

    @JsonProperty("inn")
    private String inn = null;

    @JsonProperty("registrationAddress")
    private String registrationAddress = null;

    @JsonProperty("index")
    private String index = null;

    @JsonProperty("comment")
    private String comment = null;

    @JsonProperty("motivationCorrectStatus")
    private MotivationCorrectStatus motivationCorrectStatus = null;

    @JsonProperty("documentName")
    private String documentName = null;

    @JsonProperty("passportName")
    private String passportName = null;

    @JsonProperty("startDate")
    private String startDate = null;


    /**
     * Создает пустой экземпляр класса
     */
    public MotivationProgramUser() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор мотивации
     * @param accountNumber Номер счета банка РСХБ
     * @param bikCode БИК банка
     * @param bankName Наименование филиала банка
     * @param inn ИНН
     * @param registrationAddress Адрес регистрации, в случае отсутствия ИНН
     * @param index Индекс, в случае отсутствия ИНН
     * @param comment комментарий
     * @param motivationCorrectStatus Статус корректности мотивации
     * @param documentName Имя прикрепленного документа с подтверждением участия
     * @param passportName Паспорт участника программы мотивации
     * @param startDate Дата начала действия програмы мотивации в формате ISO-8601 (ГГГГ-ММ-ДД)
     */
    public MotivationProgramUser(Long id, String accountNumber, String bikCode, String bankName, String inn, String registrationAddress, String index, String comment, MotivationCorrectStatus motivationCorrectStatus, String documentName, String passportName, String startDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.bikCode = bikCode;
        this.bankName = bankName;
        this.inn = inn;
        this.registrationAddress = registrationAddress;
        this.index = index;
        this.comment = comment;
        this.motivationCorrectStatus = motivationCorrectStatus;
        this.documentName = documentName;
        this.passportName = passportName;
        this.startDate = startDate;
    }

    /**
     * Идентификатор мотивации
    * @return Идентификатор мотивации
    **/
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Номер счета банка РСХБ
    * @return Номер счета банка РСХБ
    **/
    


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    /**
     * БИК банка
    * @return БИК банка
    **/
    


    public String getBikCode() {
        return bikCode;
    }

    public void setBikCode(String bikCode) {
        this.bikCode = bikCode;
    }


    /**
     * Наименование филиала банка
    * @return Наименование филиала банка
    **/
    


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    /**
     * ИНН
    * @return ИНН
    **/
    


    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }


    /**
     * Адрес регистрации, в случае отсутствия ИНН
    * @return Адрес регистрации, в случае отсутствия ИНН
    **/
    


    public String getRegistrationAddress() {
        return registrationAddress;
    }

    public void setRegistrationAddress(String registrationAddress) {
        this.registrationAddress = registrationAddress;
    }


    /**
     * Индекс, в случае отсутствия ИНН
    * @return Индекс, в случае отсутствия ИНН
    **/
    


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }


    /**
     * комментарий
    * @return комментарий
    **/
    


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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


    /**
     * Имя прикрепленного документа с подтверждением участия
    * @return Имя прикрепленного документа с подтверждением участия
    **/
    


    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }


    /**
     * Паспорт участника программы мотивации
    * @return Паспорт участника программы мотивации
    **/
    


    public String getPassportName() {
        return passportName;
    }

    public void setPassportName(String passportName) {
        this.passportName = passportName;
    }


    /**
     * Дата начала действия програмы мотивации в формате ISO-8601 (ГГГГ-ММ-ДД)
    * @return Дата начала действия програмы мотивации в формате ISO-8601 (ГГГГ-ММ-ДД)
    **/
    


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MotivationProgramUser motivationProgramUser = (MotivationProgramUser) o;
        return Objects.equals(this.id, motivationProgramUser.id) &&
            Objects.equals(this.accountNumber, motivationProgramUser.accountNumber) &&
            Objects.equals(this.bikCode, motivationProgramUser.bikCode) &&
            Objects.equals(this.bankName, motivationProgramUser.bankName) &&
            Objects.equals(this.inn, motivationProgramUser.inn) &&
            Objects.equals(this.registrationAddress, motivationProgramUser.registrationAddress) &&
            Objects.equals(this.index, motivationProgramUser.index) &&
            Objects.equals(this.comment, motivationProgramUser.comment) &&
            Objects.equals(this.motivationCorrectStatus, motivationProgramUser.motivationCorrectStatus) &&
            Objects.equals(this.documentName, motivationProgramUser.documentName) &&
            Objects.equals(this.passportName, motivationProgramUser.passportName) &&
            Objects.equals(this.startDate, motivationProgramUser.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, bikCode, bankName, inn, registrationAddress, index, comment, motivationCorrectStatus, documentName, passportName, startDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MotivationProgramUser {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
        sb.append("    bikCode: ").append(toIndentedString(bikCode)).append("\n");
        sb.append("    bankName: ").append(toIndentedString(bankName)).append("\n");
        sb.append("    inn: ").append(toIndentedString(inn)).append("\n");
        sb.append("    registrationAddress: ").append(toIndentedString(registrationAddress)).append("\n");
        sb.append("    index: ").append(toIndentedString(index)).append("\n");
        sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
        sb.append("    motivationCorrectStatus: ").append(toIndentedString(motivationCorrectStatus)).append("\n");
        sb.append("    documentName: ").append(toIndentedString(documentName)).append("\n");
        sb.append("    passportName: ").append(toIndentedString(passportName)).append("\n");
        sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
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

