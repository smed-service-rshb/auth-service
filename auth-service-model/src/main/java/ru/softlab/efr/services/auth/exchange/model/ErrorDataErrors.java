package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ErrorDataErrors
 */
@Validated
public class ErrorDataErrors  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("fieldName")
    private String fieldName = null;

    @JsonProperty("errorMessage")
    private String errorMessage = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ErrorDataErrors() {}

    /**
     * Создает экземпляр класса
     * @param fieldName Поле, к которому относится ошибка
     * @param errorMessage Сообщение об ошибке
     */
    public ErrorDataErrors(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    /**
     * Поле, к которому относится ошибка
    * @return Поле, к которому относится ошибка
    **/
    


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


    /**
     * Сообщение об ошибке
    * @return Сообщение об ошибке
    **/
    


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorDataErrors errorDataErrors = (ErrorDataErrors) o;
        return Objects.equals(this.fieldName, errorDataErrors.fieldName) &&
            Objects.equals(this.errorMessage, errorDataErrors.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, errorMessage);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorDataErrors {\n");
        
        sb.append("    fieldName: ").append(toIndentedString(fieldName)).append("\n");
        sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
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

