package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Типы документов мотивации:   PASSPORT - Паспорт   CHECK_FORM - Форма, подтвержденная подписью 
 */
public enum MotivationDocumentTypes {
  
  PASSPORT("PASSPORT"),
  
  CHECK_FORM("CHECK_FORM");

  private String value;

  MotivationDocumentTypes(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static MotivationDocumentTypes fromValue(String text) {
    for (MotivationDocumentTypes b : MotivationDocumentTypes.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

