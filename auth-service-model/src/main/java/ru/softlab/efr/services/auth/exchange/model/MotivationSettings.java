package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * MotivationSettings
 */
@Validated
public class MotivationSettings  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("isEnabled")
    private Boolean isEnabled = null;

    @JsonProperty("expireTime")
    private Integer expireTime = null;


    /**
     * Создает пустой экземпляр класса
     */
    public MotivationSettings() {}

    /**
     * Создает экземпляр класса
     * @param isEnabled Признак, показывающий активен ли модуль или нет
     * @param expireTime Срок действия мотиваций в месяцах
     */
    public MotivationSettings(Boolean isEnabled, Integer expireTime) {
        this.isEnabled = isEnabled;
        this.expireTime = expireTime;
    }

    /**
     * Признак, показывающий активен ли модуль или нет
    * @return Признак, показывающий активен ли модуль или нет
    **/
      @NotNull



    public Boolean isIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }


    /**
     * Срок действия мотиваций в месяцах
    * @return Срок действия мотиваций в месяцах
    **/
    


    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MotivationSettings motivationSettings = (MotivationSettings) o;
        return Objects.equals(this.isEnabled, motivationSettings.isEnabled) &&
            Objects.equals(this.expireTime, motivationSettings.expireTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isEnabled, expireTime);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MotivationSettings {\n");
        
        sb.append("    isEnabled: ").append(toIndentedString(isEnabled)).append("\n");
        sb.append("    expireTime: ").append(toIndentedString(expireTime)).append("\n");
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

