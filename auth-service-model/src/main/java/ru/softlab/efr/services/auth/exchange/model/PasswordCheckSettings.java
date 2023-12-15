package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import ru.softlab.efr.services.auth.exchange.model.PasswordCharset;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PasswordCheckSettings
 */
@Validated
public class PasswordCheckSettings  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("checkEnabled")
    private Boolean checkEnabled = null;

    @JsonProperty("maxLength")
    private Integer maxLength = null;

    @JsonProperty("minLength")
    private Integer minLength = null;

    @JsonProperty("numberOfDifferentCharacters")
    private Integer numberOfDifferentCharacters = null;

    @JsonProperty("specialCharsets")
    private String specialCharsets = null;

    @JsonProperty("enabledCharsets")
    @Valid
    private List<PasswordCharset> enabledCharsets = new ArrayList<>();

    @JsonProperty("requiredCharsets")
    @Valid
    private List<PasswordCharset> requiredCharsets = null;


    /**
     * Создает пустой экземпляр класса
     */
    public PasswordCheckSettings() {}

    /**
     * Создает экземпляр класса
     * @param checkEnabled Признак, показывающий надо ли выполнять проверку сложности паролей
     * @param maxLength Максимальная длина пароля
     * @param minLength Минимальная длина пароля
     * @param numberOfDifferentCharacters Минимальное количество различных символов в пароле
     * @param specialCharsets Набор разрешённых специальных символов
     * @param enabledCharsets Набор разрешённых множеств символов
     * @param requiredCharsets Набор обязательных множеств символов
     */
    public PasswordCheckSettings(Boolean checkEnabled, Integer maxLength, Integer minLength, Integer numberOfDifferentCharacters, String specialCharsets, List<PasswordCharset> enabledCharsets, List<PasswordCharset> requiredCharsets) {
        this.checkEnabled = checkEnabled;
        this.maxLength = maxLength;
        this.minLength = minLength;
        this.numberOfDifferentCharacters = numberOfDifferentCharacters;
        this.specialCharsets = specialCharsets;
        this.enabledCharsets = enabledCharsets;
        this.requiredCharsets = requiredCharsets;
    }

    /**
     * Признак, показывающий надо ли выполнять проверку сложности паролей
    * @return Признак, показывающий надо ли выполнять проверку сложности паролей
    **/
      @NotNull



    public Boolean isCheckEnabled() {
        return checkEnabled;
    }

    public void setCheckEnabled(Boolean checkEnabled) {
        this.checkEnabled = checkEnabled;
    }


    /**
     * Максимальная длина пароля
    * @return Максимальная длина пароля
    **/
      @NotNull



    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }


    /**
     * Минимальная длина пароля
    * @return Минимальная длина пароля
    **/
      @NotNull



    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }


    /**
     * Минимальное количество различных символов в пароле
    * @return Минимальное количество различных символов в пароле
    **/
      @NotNull



    public Integer getNumberOfDifferentCharacters() {
        return numberOfDifferentCharacters;
    }

    public void setNumberOfDifferentCharacters(Integer numberOfDifferentCharacters) {
        this.numberOfDifferentCharacters = numberOfDifferentCharacters;
    }


    /**
     * Набор разрешённых специальных символов
    * @return Набор разрешённых специальных символов
    **/
      @NotNull

@Size(max=255) 

    public String getSpecialCharsets() {
        return specialCharsets;
    }

    public void setSpecialCharsets(String specialCharsets) {
        this.specialCharsets = specialCharsets;
    }


    public PasswordCheckSettings addEnabledCharsetsItem(PasswordCharset enabledCharsetsItem) {
        this.enabledCharsets.add(enabledCharsetsItem);
        return this;
    }

    /**
     * Набор разрешённых множеств символов
    * @return Набор разрешённых множеств символов
    **/
      @NotNull

  @Valid


    public List<PasswordCharset> getEnabledCharsets() {
        return enabledCharsets;
    }

    public void setEnabledCharsets(List<PasswordCharset> enabledCharsets) {
        this.enabledCharsets = enabledCharsets;
    }


    public PasswordCheckSettings addRequiredCharsetsItem(PasswordCharset requiredCharsetsItem) {
        if (this.requiredCharsets == null) {
            this.requiredCharsets = new ArrayList<>();
        }
        this.requiredCharsets.add(requiredCharsetsItem);
        return this;
    }

    /**
     * Набор обязательных множеств символов
    * @return Набор обязательных множеств символов
    **/
    
  @Valid


    public List<PasswordCharset> getRequiredCharsets() {
        return requiredCharsets;
    }

    public void setRequiredCharsets(List<PasswordCharset> requiredCharsets) {
        this.requiredCharsets = requiredCharsets;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PasswordCheckSettings passwordCheckSettings = (PasswordCheckSettings) o;
        return Objects.equals(this.checkEnabled, passwordCheckSettings.checkEnabled) &&
            Objects.equals(this.maxLength, passwordCheckSettings.maxLength) &&
            Objects.equals(this.minLength, passwordCheckSettings.minLength) &&
            Objects.equals(this.numberOfDifferentCharacters, passwordCheckSettings.numberOfDifferentCharacters) &&
            Objects.equals(this.specialCharsets, passwordCheckSettings.specialCharsets) &&
            Objects.equals(this.enabledCharsets, passwordCheckSettings.enabledCharsets) &&
            Objects.equals(this.requiredCharsets, passwordCheckSettings.requiredCharsets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkEnabled, maxLength, minLength, numberOfDifferentCharacters, specialCharsets, enabledCharsets, requiredCharsets);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PasswordCheckSettings {\n");
        
        sb.append("    checkEnabled: ").append(toIndentedString(checkEnabled)).append("\n");
        sb.append("    maxLength: ").append(toIndentedString(maxLength)).append("\n");
        sb.append("    minLength: ").append(toIndentedString(minLength)).append("\n");
        sb.append("    numberOfDifferentCharacters: ").append(toIndentedString(numberOfDifferentCharacters)).append("\n");
        sb.append("    specialCharsets: ").append(toIndentedString(specialCharsets)).append("\n");
        sb.append("    enabledCharsets: ").append(toIndentedString(enabledCharsets)).append("\n");
        sb.append("    requiredCharsets: ").append(toIndentedString(requiredCharsets)).append("\n");
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

