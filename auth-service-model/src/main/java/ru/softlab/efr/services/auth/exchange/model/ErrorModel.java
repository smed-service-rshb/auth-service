package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.CheckModel;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Описание ошибки
 */
@Validated
public class ErrorModel  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("errors")
    @Valid
    private List<CheckModel> errors = new ArrayList<>();


    /**
     * Создает пустой экземпляр класса
     */
    public ErrorModel() {}

    /**
     * Создает экземпляр класса
     * @param errors 
     */
    public ErrorModel(List<CheckModel> errors) {
        this.errors = errors;
    }

    public ErrorModel addErrorsItem(CheckModel errorsItem) {
        this.errors.add(errorsItem);
        return this;
    }

    /**
    * Get errors
    * @return 
    **/
      @NotNull

  @Valid


    public List<CheckModel> getErrors() {
        return errors;
    }

    public void setErrors(List<CheckModel> errors) {
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
        ErrorModel errorModel = (ErrorModel) o;
        return Objects.equals(this.errors, errorModel.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorModel {\n");
        
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

