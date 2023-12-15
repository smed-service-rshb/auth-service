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
 * Параметры запроса авторизации 
 */
@Validated
public class MobileLoginRequest  implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * Метод авторизации 
   */
  public enum AuthMethodEnum {
    NAME_AND_PASSWORD("AUTH_METH_NAME_AND_PASSWORD"),
    
    SHORT_CODE("AUTH_METH_SHORT_CODE");

    private String value;

    AuthMethodEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AuthMethodEnum fromValue(String text) {
      for (AuthMethodEnum b : AuthMethodEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

    @JsonProperty("authMethod")
    private AuthMethodEnum authMethod = null;

    @JsonProperty("login")
    private String login = null;

    @JsonProperty("password")
    private String password = null;

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
    public MobileLoginRequest() {}

    /**
     * Создает экземпляр класса
     * @param authMethod Метод авторизации 
     * @param login Логин или токен 
     * @param password Пароль или пин 
     * @param identifierDevice Идентификатор устройства 
     * @param platform Платформа 
     */
    public MobileLoginRequest(AuthMethodEnum authMethod, String login, String password, String identifierDevice, PlatformEnum platform) {
        this.authMethod = authMethod;
        this.login = login;
        this.password = password;
        this.identifierDevice = identifierDevice;
        this.platform = platform;
    }

    /**
     * Метод авторизации 
    * @return Метод авторизации 
    **/
      @NotNull



    public AuthMethodEnum getAuthMethod() {
        return authMethod;
    }

    public void setAuthMethod(AuthMethodEnum authMethod) {
        this.authMethod = authMethod;
    }


    /**
     * Логин или токен 
    * @return Логин или токен 
    **/
      @NotNull



    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    /**
     * Пароль или пин 
    * @return Пароль или пин 
    **/
      @NotNull



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Идентификатор устройства 
    * @return Идентификатор устройства 
    **/
    


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
        MobileLoginRequest mobileLoginRequest = (MobileLoginRequest) o;
        return Objects.equals(this.authMethod, mobileLoginRequest.authMethod) &&
            Objects.equals(this.login, mobileLoginRequest.login) &&
            Objects.equals(this.password, mobileLoginRequest.password) &&
            Objects.equals(this.identifierDevice, mobileLoginRequest.identifierDevice) &&
            Objects.equals(this.platform, mobileLoginRequest.platform);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authMethod, login, password, identifierDevice, platform);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MobileLoginRequest {\n");
        
        sb.append("    authMethod: ").append(toIndentedString(authMethod)).append("\n");
        sb.append("    login: ").append(toIndentedString(login)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
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

