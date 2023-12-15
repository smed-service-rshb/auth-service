package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Контейнер с результатами одной проверки
 */
@Validated
public class CheckModel  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("key")
    private String key = null;

    @JsonProperty("value")
    private String value = null;


    /**
     * Создает пустой экземпляр класса
     */
    public CheckModel() {}

    /**
     * Создает экземпляр класса
     * @param key Поле или иной объект, подверженный проверке
     * @param value Результат проверки, описывающий выявленные отклонения от ТЗ
     */
    public CheckModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Поле или иной объект, подверженный проверке
    * @return Поле или иной объект, подверженный проверке
    **/
    


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    /**
     * Результат проверки, описывающий выявленные отклонения от ТЗ
    * @return Результат проверки, описывающий выявленные отклонения от ТЗ
    **/
    


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CheckModel checkModel = (CheckModel) o;
        return Objects.equals(this.key, checkModel.key) &&
            Objects.equals(this.value, checkModel.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CheckModel {\n");
        
        sb.append("    key: ").append(toIndentedString(key)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

