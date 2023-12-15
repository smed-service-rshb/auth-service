package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * GroupData
 */
@Validated
public class GroupData  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("code")
    private String code = null;


    /**
     * Создает пустой экземпляр класса
     */
    public GroupData() {}

    /**
     * Создает экземпляр класса
     * @param id Идентификатор записи справочника группы пользователя
     * @param name Наименование записи справочника группы пользователя
     * @param code Код группы записи справочника
     */
    public GroupData(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    /**
     * Идентификатор записи справочника группы пользователя
    * @return Идентификатор записи справочника группы пользователя
    **/
    


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /**
     * Наименование записи справочника группы пользователя
    * @return Наименование записи справочника группы пользователя
    **/
    


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    /**
     * Код группы записи справочника
    * @return Код группы записи справочника
    **/
    


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupData groupData = (GroupData) o;
        return Objects.equals(this.id, groupData.id) &&
            Objects.equals(this.name, groupData.name) &&
            Objects.equals(this.code, groupData.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GroupData {\n");
        
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
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

