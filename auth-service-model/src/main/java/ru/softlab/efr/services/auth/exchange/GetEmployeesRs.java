package ru.softlab.efr.services.auth.exchange;

import ru.softlab.efr.services.auth.UserListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Тело ответа на запрос списка сотрудников
 *
 * @author niculichev
 * @since 20.04.2017
 */
public class GetEmployeesRs {
    private List<UserListData> employees = new ArrayList<>();

    /**
     * @return список данных по сотрудникам
     */
    public List<UserListData> getEmployees() {
        return employees;
    }

    public void setEmployees(List<UserListData> employees) {
        this.employees = employees;
    }
}
