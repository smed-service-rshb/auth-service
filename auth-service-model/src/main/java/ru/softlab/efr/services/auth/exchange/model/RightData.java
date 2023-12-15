package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * RightData
 */
@Validated
public class RightData  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("externalId")
    private String externalId = null;


    /**
     * Создает пустой экземпляр класса
     */
    public RightData() {}

    /**
     * Создает экземпляр класса
     * @param description Описание права
     * @param externalId Ключ права
     */
    public RightData(String description, String externalId) {
        this.description = description;
        this.externalId = externalId;
    }

    /**
     * Описание права
    * @return Описание права
    **/
    


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Ключ права
    * @return Ключ права
    **/
    


    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RightData rightData = (RightData) o;
        return Objects.equals(this.description, rightData.description) &&
            Objects.equals(this.externalId, rightData.externalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, externalId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RightData {\n");
        
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    externalId: ").append(toIndentedString(externalId)).append("\n");
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

