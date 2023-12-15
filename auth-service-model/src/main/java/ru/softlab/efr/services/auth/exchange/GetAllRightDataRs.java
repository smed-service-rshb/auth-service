package ru.softlab.efr.services.auth.exchange;

import ru.softlab.efr.services.auth.RightData;

import java.util.List;

/**
 * Ответ на зарос полного списка прав
 *
 * @author akrenev
 * @since 15.02.2018
 */
public class GetAllRightDataRs {
    private List<RightData> rights;

    /**
     * Конструктор
     */
    public GetAllRightDataRs() {
    }

    /**
     * Конструктор
     *
     * @param rights права
     */
    public GetAllRightDataRs(List<RightData> rights) {
        this.rights = rights;
    }

    /**
     * Получить права
     *
     * @return права
     */
    public List<RightData> getRights() {
        return rights;
    }

    /**
     * Задать права
     *
     * @param rights права
     */
    public void setRights(List<RightData> rights) {
        this.rights = rights;
    }
}
