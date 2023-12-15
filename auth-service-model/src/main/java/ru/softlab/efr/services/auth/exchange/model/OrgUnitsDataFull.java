package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.OrgUnitDataFull;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * OrgUnitsDataFull
 */
@Validated
public class OrgUnitsDataFull  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("orgUnits")
    @Valid
    private List<OrgUnitDataFull> orgUnits = null;


    /**
     * Создает пустой экземпляр класса
     */
    public OrgUnitsDataFull() {}

    /**
     * Создает экземпляр класса
     * @param orgUnits Список орг. структур
     */
    public OrgUnitsDataFull(List<OrgUnitDataFull> orgUnits) {
        this.orgUnits = orgUnits;
    }

    public OrgUnitsDataFull addOrgUnitsItem(OrgUnitDataFull orgUnitsItem) {
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


    public List<OrgUnitDataFull> getOrgUnits() {
        return orgUnits;
    }

    public void setOrgUnits(List<OrgUnitDataFull> orgUnits) {
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
        OrgUnitsDataFull orgUnitsDataFull = (OrgUnitsDataFull) o;
        return Objects.equals(this.orgUnits, orgUnitsDataFull.orgUnits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgUnits);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OrgUnitsDataFull {\n");
        
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

