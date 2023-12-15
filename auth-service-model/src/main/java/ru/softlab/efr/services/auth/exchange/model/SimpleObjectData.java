package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * SimpleObjectData
 */
@Validated
public class SimpleObjectData  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private BigDecimal id = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("name")
    private String name = null;


    /**
     * Создает пустой экземпляр класса
     */
    public SimpleObjectData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор сущности
     * @param code Код
     * @param name Наименование
     */
    public SimpleObjectData(BigDecimal id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    /**
     * Идентификатор сущности
    * @return Идентификатор сущности
    **/
    
  @Valid


    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }


    /**
     * Код
    * @return Код
    **/
    


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    /**
     * Наименование
    * @return Наименование
    **/
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleObjectData simpleObjectData = (SimpleObjectData) o;
        return Objects.equals(this.id, simpleObjectData.id) &&
            Objects.equals(this.code, simpleObjectData.code) &&
            Objects.equals(this.name, simpleObjectData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SimpleObjectData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

