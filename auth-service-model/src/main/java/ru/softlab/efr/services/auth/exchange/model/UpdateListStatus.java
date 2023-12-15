package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UpdateListStatus
 */
@Validated
public class UpdateListStatus  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("status")
    private String status = null;


    /**
     * Создает пустой экземпляр класса
     */
    public UpdateListStatus() {}

    /**
     * Создает экземпляр класса
     * @param status 
     */
    public UpdateListStatus(String status) {
        this.status = status;
    }

    /**
    * Get status
    * @return 
    **/
    


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateListStatus updateListStatus = (UpdateListStatus) o;
        return Objects.equals(this.status, updateListStatus.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UpdateListStatus {\n");
        
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

