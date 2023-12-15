package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.ErrorDataErrors;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ErrorData
 */
@Validated
public class ErrorData  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("errors")
    @Valid
    private List<ErrorDataErrors> errors = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ErrorData() {}

    /**
     * Создает экземпляр класса
     * @param errors 
     */
    public ErrorData(List<ErrorDataErrors> errors) {
        this.errors = errors;
    }

    public ErrorData addErrorsItem(ErrorDataErrors errorsItem) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(errorsItem);
        return this;
    }

    /**
    * Get errors
    * @return 
    **/
    
  @Valid


    public List<ErrorDataErrors> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDataErrors> errors) {
        this.errors = errors;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorData errorData = (ErrorData) o;
        return Objects.equals(this.errors, errorData.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorData {\n");
        
        sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
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

