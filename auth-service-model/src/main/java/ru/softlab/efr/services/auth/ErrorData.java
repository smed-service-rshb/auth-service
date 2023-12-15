package ru.softlab.efr.services.auth;

/**
 * Данные ошибки валидации
 *
 * @author niculichev
 * @since 20.04.2017
 */
public class ErrorData {
    private String fieldName;
    private String errorMessage;

    public ErrorData(){
    }

    public ErrorData(String fieldName, String errorMessage){
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    /**
     * @return название поля, в котором допущена ошибка, либо null если ошибка формы
     */
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return содержание ошибки
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
