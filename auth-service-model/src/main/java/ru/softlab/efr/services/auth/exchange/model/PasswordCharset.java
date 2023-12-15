package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Множество групп символов, использующихся для настройки требований к паролям пользователей.   * DIGIT - Цифры   * LOWERCASE_LATIN - Латинские прописные буквы   * UPPERCASE_LATIN - Латинские заглавные буквы   * SPECIAL - Специальные символы 
 */
public enum PasswordCharset {
  
  DIGIT("DIGIT"),
  
  LOWERCASE_LATIN("LOWERCASE_LATIN"),
  
  UPPERCASE_LATIN("UPPERCASE_LATIN"),
  
  SPECIAL("SPECIAL");

  private String value;

  PasswordCharset(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static PasswordCharset fromValue(String text) {
    for (PasswordCharset b : PasswordCharset.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

