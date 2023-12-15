package ru.softlab.efr.services.auth.exchange;

import ru.softlab.efr.services.auth.ErrorData;

import java.util.ArrayList;
import java.util.List;

/**
 * Тело ответа на запрос с ошибочными(невалидными) данными
 *
 * @author niculichev
 * @since 20.04.2017
 */
public class ErrorsDataRs {
    private List<ErrorData> errors = new ArrayList<>();

    /**
     * @return данные по ошибкам
     */
    public List<ErrorData> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorData> errors) {
        this.errors = errors;
    }
}
