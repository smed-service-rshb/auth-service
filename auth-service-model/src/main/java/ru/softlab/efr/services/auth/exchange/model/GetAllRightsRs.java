package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.RightData;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GetAllRightsRs
 */
@Validated
public class GetAllRightsRs  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("rights")
    @Valid
    private List<RightData> rights = null;


    /**
     * Создает пустой экземпляр класса
     */
    public GetAllRightsRs() {}

    /**
     * Создает экземпляр класса
     * @param rights 
     */
    public GetAllRightsRs(List<RightData> rights) {
        this.rights = rights;
    }

    public GetAllRightsRs addRightsItem(RightData rightsItem) {
        if (this.rights == null) {
            this.rights = new ArrayList<>();
        }
        this.rights.add(rightsItem);
        return this;
    }

    /**
    * Get rights
    * @return 
    **/
    
  @Valid


    public List<RightData> getRights() {
        return rights;
    }

    public void setRights(List<RightData> rights) {
        this.rights = rights;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetAllRightsRs getAllRightsRs = (GetAllRightsRs) o;
        return Objects.equals(this.rights, getAllRightsRs.rights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rights);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GetAllRightsRs {\n");
        
        sb.append("    rights: ").append(toIndentedString(rights)).append("\n");
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

