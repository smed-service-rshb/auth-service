package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * LockEmployeeRq
 */
@Validated
public class LockEmployeeRq  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("description")
    private String description = null;


    /**
     * Создает пустой экземпляр класса
     */
    public LockEmployeeRq() {}

    /**
     * Создает экземпляр класса
     * @param description Описание причины блокировки
     */
    public LockEmployeeRq(String description) {
        this.description = description;
    }

    /**
     * Описание причины блокировки
    * @return Описание причины блокировки
    **/
    


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LockEmployeeRq lockEmployeeRq = (LockEmployeeRq) o;
        return Objects.equals(this.description, lockEmployeeRq.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LockEmployeeRq {\n");
        
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

