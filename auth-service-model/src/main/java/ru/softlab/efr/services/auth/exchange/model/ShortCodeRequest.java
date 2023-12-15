package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Информация о коротком коде 
 */
@Validated
public class ShortCodeRequest  implements Serializable {
  private static final long serialVersionUID = 1L;

    @JsonProperty("shortCode")
    private String shortCode = null;

    @JsonProperty("sessionId")
    private String sessionId = null;

    @JsonProperty("identifierDevice")
    private String identifierDevice = null;

  /**
   * Платформа 
   */
  public enum PlatformEnum {
    ANDROID("ANDROID"),
    
    IOS("IOS");

    private String value;

    PlatformEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PlatformEnum fromValue(String text) {
      for (PlatformEnum b : PlatformEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    @JsonProperty("platform")
    private PlatformEnum platform = null;


    /**
     * Создает пустой экземпляр класса
     */
    public ShortCodeRequest() {}

    /**
     * Создает экземпляр класса
     * @param shortCode Код доступа 
     * @param sessionId id-сессии 
     * @param identifierDevice Идентификатор устройства 
     * @param platform Платформа 
     */
    public ShortCodeRequest(String shortCode, String sessionId, String identifierDevice, PlatformEnum platform) {
        this.shortCode = shortCode;
        this.sessionId = sessionId;
        this.identifierDevice = identifierDevice;
        this.platform = platform;
    }

    /**
     * Код доступа 
    * @return Код доступа 
    **/
      @NotNull



    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }


    /**
     * id-сессии 
    * @return id-сессии 
    **/
      @NotNull



    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Идентификатор устройства 
    * @return Идентификатор устройства 
    **/
      @NotNull



    public String getIdentifierDevice() {
        return identifierDevice;
    }

    public void setIdentifierDevice(String identifierDevice) {
        this.identifierDevice = identifierDevice;
    }


    /**
     * Платформа 
    * @return Платформа 
    **/
      @NotNull



    public PlatformEnum getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformEnum platform) {
        this.platform = platform;
    }


  @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShortCodeRequest shortCodeRequest = (ShortCodeRequest) o;
        return Objects.equals(this.shortCode, shortCodeRequest.shortCode) &&
            Objects.equals(this.sessionId, shortCodeRequest.sessionId) &&
            Objects.equals(this.identifierDevice, shortCodeRequest.identifierDevice) &&
            Objects.equals(this.platform, shortCodeRequest.platform);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortCode, sessionId, identifierDevice, platform);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ShortCodeRequest {\n");
        
        sb.append("    shortCode: ").append(toIndentedString(shortCode)).append("\n");
        sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
        sb.append("    identifierDevice: ").append(toIndentedString(identifierDevice)).append("\n");
        sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
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

