package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * EntityIdRs
 */
@Validated
public class EntityIdRs  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id = null;


    /**
     * Создает пустой экземпляр класса
     */
    public EntityIdRs() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор возвращаемого объекта, например, роли
     */
    public EntityIdRs(Long id) {
        this.id = id;
    }

    /**
     * Идентификатор возвращаемого объекта, например, роли
    * @return Идентификатор возвращаемого объекта, например, роли
    **/
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityIdRs entityIdRs = (EntityIdRs) o;
        return Objects.equals(this.id, entityIdRs.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EntityIdRs {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

