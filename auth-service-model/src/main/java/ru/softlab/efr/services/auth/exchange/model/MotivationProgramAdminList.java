package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.MotivationProgramAdmin;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * MotivationProgramAdminList
 */
@Validated
public class MotivationProgramAdminList  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("elements")
    @Valid
    private List<MotivationProgramAdmin> elements = null;


    /**
     * Создает пустой экземпляр класса
     */
    public MotivationProgramAdminList() {}

    /**
     * Создает экземпляр класса
     * @param elements 
     */
    public MotivationProgramAdminList(List<MotivationProgramAdmin> elements) {
        this.elements = elements;
    }

    public MotivationProgramAdminList addElementsItem(MotivationProgramAdmin elementsItem) {
        if (this.elements == null) {
            this.elements = new ArrayList<>();
        }
        this.elements.add(elementsItem);
        return this;
    }

    /**
    * Get elements
    * @return 
    **/
    
  @Valid


    public List<MotivationProgramAdmin> getElements() {
        return elements;
    }

    public void setElements(List<MotivationProgramAdmin> elements) {
        this.elements = elements;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MotivationProgramAdminList motivationProgramAdminList = (MotivationProgramAdminList) o;
        return Objects.equals(this.elements, motivationProgramAdminList.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MotivationProgramAdminList {\n");
        
        sb.append("    elements: ").append(toIndentedString(elements)).append("\n");
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

