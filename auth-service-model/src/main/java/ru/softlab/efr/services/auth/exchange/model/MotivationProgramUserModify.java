package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Модель создания или модификации программы мотивации у пользователя
 */
@Validated
public class MotivationProgramUserModify  implements Serializable {
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

    @JsonProperty("startDate")
    private LocalDate startDate = null;


    /**
     * Создает пустой экземпляр класса
     */
    public MotivationProgramUserModify() {}

    /**
     * Создает экземпляр класса
     * @param accountNumber Номер счета банка РСХБ
     * @param bikCode БИК банка
     * @param bankName Наименование филиала банка
     * @param inn ИНН
     * @param registrationAddress Адрес регистрации, в случае отсутствия ИНН
     * @param index Индекс, в случае отсутствия ИНН
     * @param startDate Дата начала действия програмы мотивации
     */
    public MotivationProgramUserModify(String accountNumber, String bikCode, String bankName, String inn, String registrationAddress, String index, LocalDate startDate) {
        this.accountNumber = accountNumber;
        this.bikCode = bikCode;
        this.bankName = bankName;
        this.inn = inn;
        this.registrationAddress = registrationAddress;
        this.index = index;
        this.startDate = startDate;
    }

    /**
     * Номер счета банка РСХБ
    * @return Номер счета банка РСХБ
    **/
      @NotNull

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
      @NotNull

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
      @NotNull

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
        MotivationProgramUserModify motivationProgramUserModify = (MotivationProgramUserModify) o;
        return Objects.equals(this.accountNumber, motivationProgramUserModify.accountNumber) &&
            Objects.equals(this.bikCode, motivationProgramUserModify.bikCode) &&
            Objects.equals(this.bankName, motivationProgramUserModify.bankName) &&
            Objects.equals(this.inn, motivationProgramUserModify.inn) &&
            Objects.equals(this.registrationAddress, motivationProgramUserModify.registrationAddress) &&
            Objects.equals(this.index, motivationProgramUserModify.index) &&
            Objects.equals(this.startDate, motivationProgramUserModify.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, bikCode, bankName, inn, registrationAddress, index, startDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MotivationProgramUserModify {\n");
        
        sb.append("    accountNumber: ").append(toIndentedString(accountNumber)).append("\n");
        sb.append("    bikCode: ").append(toIndentedString(bikCode)).append("\n");
        sb.append("    bankName: ").append(toIndentedString(bankName)).append("\n");
        sb.append("    inn: ").append(toIndentedString(inn)).append("\n");
        sb.append("    registrationAddress: ").append(toIndentedString(registrationAddress)).append("\n");
        sb.append("    index: ").append(toIndentedString(index)).append("\n");
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

