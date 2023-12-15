package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Организационная штатная структура
 */
@Validated
public class OrgUnitData  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("branch")
    private String branch = null;

    @JsonProperty("office")
    private String office = null;


    /**
     * Создает пустой экземпляр класса
     */
    public OrgUnitData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор записи
     * @param branch Название регионального филиала (РФ); заполняется, если type = РФ.
     * @param office Номер внутреннего структурного подразделения (ВСП); заполняется, если type = ВСП.
     */
    public OrgUnitData(Long id, String branch, String office) {
        this.id = id;
        this.branch = branch;
        this.office = office;
    }

    /**
     * Идентификатор записи
    * @return Идентификатор записи
    **/
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Название регионального филиала (РФ); заполняется, если type = РФ.
    * @return Название регионального филиала (РФ); заполняется, если type = РФ.
    **/
    
@Size(min=1,max=255) 

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }


    /**
     * Номер внутреннего структурного подразделения (ВСП); заполняется, если type = ВСП.
    * @return Номер внутреннего структурного подразделения (ВСП); заполняется, если type = ВСП.
    **/
    
@Size(min=1,max=255) 

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrgUnitData orgUnitData = (OrgUnitData) o;
        return Objects.equals(this.id, orgUnitData.id) &&
            Objects.equals(this.branch, orgUnitData.branch) &&
            Objects.equals(this.office, orgUnitData.office);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, branch, office);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OrgUnitData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    branch: ").append(toIndentedString(branch)).append("\n");
        sb.append("    office: ").append(toIndentedString(office)).append("\n");
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

