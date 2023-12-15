package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import ru.softlab.efr.services.auth.exchange.model.MotivationCorrectStatus;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Форма редактирования программы мотивации
 */
@Validated
public class MotivationSettingsEditAdmin  implements Serializable {
  private static final long serialVersionUID = 1L;

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

    @JsonProperty("startDate")
    private LocalDate startDate = null;


    /**
     * Создает пустой экземпляр класса
     */
    public MotivationSettingsEditAdmin() {}

    /**
     * Создает экземпляр класса
     * @param accountNumber Номер счета банка РСХБ
     * @param bikCode БИК банка
     * @param bankName Наименование филиала банка
     * @param inn ИНН
     * @param registrationAddress Адрес регистрации, в случае отсутствия ИНН
     * @param index Индекс, в случае отсутствия ИНН
     * @param comment комментарий
     * @param motivationCorrectStatus Статус корректности мотивации
     * @param startDate Дата начала действия програмы мотивации
     */
    public MotivationSettingsEditAdmin(String accountNumber, String bikCode, String bankName, String inn, String registrationAddress, String index, String comment, MotivationCorrectStatus motivationCorrectStatus, LocalDate startDate) {
        this.accountNumber = accountNumber;
        this.bikCode = bikCode;
        this.bankName = bankName;
        this.inn = inn;
        this.registrationAddress = registrationAddress;
        this.index = index;
        this.comment = comment;
        this.motivationCorrectStatus = motivationCorrectStatus;
        this.startDate = startDate;
    }

    /**
     * Номер счета банка РСХБ
    * @return Номер счета банка РСХБ
    **/
    
@Pattern(regexp="(^[0-9]{20})") 

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
    
@Pattern(regexp="(^[0-9]{9})") 

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
    
@Pattern(regexp="(^(.{0,250}$))") 

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
    
@Pattern(regexp="(^[0-9]{12})") 

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
    
@Pattern(regexp="(^(.{0,250}$))") 

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
    
@Pattern(regexp="(^[0-9]{6})") 

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
     * Дата начала действия програмы мотивации
    * @return Дата начала действия програмы мотивации
    **/
    
  @Valid


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
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
        MotivationSettingsEditAdmin motivationSettingsEditAdmin = (MotivationSettingsEditAdmin) o;
        return Objects.equals(this.accountNumber, motivationSettingsEditAdmin.accountNumber) &&
            Objects.equals(this.bikCode, motivationSettingsEditAdmin.bikCode) &&
            Objects.equals(this.bankName, motivationSettingsEditAdmin.bankName) &&
            Objects.equals(this.inn, motivationSettingsEditAdmin.inn) &&
            Objects.equals(this.registrationAddress, motivationSettingsEditAdmin.registrationAddress) &&
            Objects.equals(this.index, motivationSettingsEditAdmin.index) &&
            Objects.equals(this.comment, motivationSettingsEditAdmin.comment) &&
            Objects.equals(this.motivationCorrectStatus, motivationSettingsEditAdmin.motivationCorrectStatus) &&
            Objects.equals(this.startDate, motivationSettingsEditAdmin.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, bikCode, bankName, inn, registrationAddress, index, comment, motivationCorrectStatus, startDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MotivationSettingsEditAdmin {\n");
        
        sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
        sb.append("    bikCode: ").append(toIndentedString(bikCode)).append("\n");
        sb.append("    bankName: ").append(toIndentedString(bankName)).append("\n");
        sb.append("    inn: ").append(toIndentedString(inn)).append("\n");
        sb.append("    registrationAddress: ").append(toIndentedString(registrationAddress)).append("\n");
        sb.append("    index: ").append(toIndentedString(index)).append("\n");
        sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
        sb.append("    motivationCorrectStatus: ").append(toIndentedString(motivationCorrectStatus)).append("\n");
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

