package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.OrgUnitData;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * OrgUnitsData
 */
@Validated
public class OrgUnitsData  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("orgUnits")
    @Valid
    private List<OrgUnitData> orgUnits = null;


    /**
     * Создает пустой экземпляр класса
     */
    public OrgUnitsData() {}

    /**
     * Создает экземпляр класса
     * @param orgUnits Список орг. структур
     */
    public OrgUnitsData(List<OrgUnitData> orgUnits) {
        this.orgUnits = orgUnits;
    }

    public OrgUnitsData addOrgUnitsItem(OrgUnitData orgUnitsItem) {
        if (this.orgUnits == null) {
            this.orgUnits = new ArrayList<>();
        }
        this.orgUnits.add(orgUnitsItem);
        return this;
    }

    /**
     * Список орг. структур
    * @return Список орг. структур
    **/
    
  @Valid


    public List<OrgUnitData> getOrgUnits() {
        return orgUnits;
    }

    public void setOrgUnits(List<OrgUnitData> orgUnits) {
        this.orgUnits = orgUnits;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrgUnitsData orgUnitsData = (OrgUnitsData) o;
        return Objects.equals(this.orgUnits, orgUnitsData.orgUnits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgUnits);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OrgUnitsData {\n");
        
        sb.append("    orgUnits: ").append(toIndentedString(orgUnits)).append("\n");
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

