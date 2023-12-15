package ru.softlab.efr.services.auth.exchange.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Статус корректности мотивации:   CORRECT - Признак корректности   INCORRECT - Признак некорректности   NOT_CHECKED - Не проверено 
 */
public enum MotivationCorrectStatus {
  
  CORRECT("CORRECT"),
  
  INCORRECT("INCORRECT"),
  
  NOT_CHECKED("NOT_CHECKED");

  private String value;

  MotivationCorrectStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static MotivationCorrectStatus fromValue(String text) {
    for (MotivationCorrectStatus b : MotivationCorrectStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}

