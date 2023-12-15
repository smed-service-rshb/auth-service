package ru.softlab.efr.services.auth.model;

/**
 * Тип организационной штатной структуры.
 */
public enum  OrgUnitType{

  /**
   * Региональный филиал
   */
  BRANCH("РФ"),

  /**
   * Внутреннее структурное подразделение
   */
  OFFICE("ВСП");

  private final String text;

  OrgUnitType(final String text) {
    this.text = text;
  }
}