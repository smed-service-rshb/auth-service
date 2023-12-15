package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Офис
 */
@Validated
public class OfficeData  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("officeId")
    private Long officeId = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("city")
    private String city = null;

    @JsonProperty("address")
    private String address = null;


    /**
     * Создает пустой экземпляр класса
     */
    public OfficeData() {}

    /**
     * Создает экземпляр класса
     * @param officeId Идентификатор офиса
     * @param name Номер внутреннего структурного подразделения (ВСП)
     * @param city Город
     * @param address Адрес структурного подразделения
     */
    public OfficeData(Long officeId, String name, String city, String address) {
        this.officeId = officeId;
        this.name = name;
        this.city = city;
        this.address = address;
    }

    /**
     * Идентификатор офиса
    * @return Идентификатор офиса
    **/
    


    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }


    /**
     * Номер внутреннего структурного подразделения (ВСП)
    * @return Номер внутреннего структурного подразделения (ВСП)
    **/
    
@Size(min=1,max=255) 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Город
    * @return Город
    **/
    
@Size(min=1,max=255) 

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    /**
     * Адрес структурного подразделения
    * @return Адрес структурного подразделения
    **/
    


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OfficeData officeData = (OfficeData) o;
        return Objects.equals(this.officeId, officeData.officeId) &&
            Objects.equals(this.name, officeData.name) &&
            Objects.equals(this.city, officeData.city) &&
            Objects.equals(this.address, officeData.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(officeId, name, city, address);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OfficeData {\n");
        
        sb.append("    officeId: ").append(toIndentedString(officeId)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    city: ").append(toIndentedString(city)).append("\n");
        sb.append("    address: ").append(toIndentedString(address)).append("\n");
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

