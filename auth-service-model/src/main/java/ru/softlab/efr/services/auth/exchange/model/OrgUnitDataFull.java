package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.OfficeData;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Организационная штатная структура
 */
@Validated
public class OrgUnitDataFull  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("branchId")
    private Long branchId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("offices")
    @Valid
    private List<OfficeData> offices = null;


    /**
     * Создает пустой экземпляр класса
     */
    public OrgUnitDataFull() {}

    /**
     * Создает экземпляр класса
     * @param branchId Идентификатор филиала
     * @param name Название регионального филиала (РФ)
     * @param offices Список офисов
     */
    public OrgUnitDataFull(Long branchId, String name, List<OfficeData> offices) {
        this.branchId = branchId;
        this.name = name;
        this.offices = offices;
    }

    /**
     * Идентификатор филиала
    * @return Идентификатор филиала
    **/
    


    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }


    /**
     * Название регионального филиала (РФ)
    * @return Название регионального филиала (РФ)
    **/
    
@Size(min=1,max=255) 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public OrgUnitDataFull addOfficesItem(OfficeData officesItem) {
        if (this.offices == null) {
            this.offices = new ArrayList<>();
        }
        this.offices.add(officesItem);
        return this;
    }

    /**
     * Список офисов
    * @return Список офисов
    **/
    
  @Valid


    public List<OfficeData> getOffices() {
        return offices;
    }

    public void setOffices(List<OfficeData> offices) {
        this.offices = offices;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrgUnitDataFull orgUnitDataFull = (OrgUnitDataFull) o;
        return Objects.equals(this.branchId, orgUnitDataFull.branchId) &&
            Objects.equals(this.name, orgUnitDataFull.name) &&
            Objects.equals(this.offices, orgUnitDataFull.offices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchId, name, offices);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OrgUnitDataFull {\n");
        
        sb.append("    branchId: ").append(toIndentedString(branchId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    offices: ").append(toIndentedString(offices)).append("\n");
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

