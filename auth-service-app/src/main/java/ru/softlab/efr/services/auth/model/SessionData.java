package ru.softlab.efr.services.auth.model;


/**
 * Клас для хранения данных о текущей сесии и данных о пользователя
 */
public class SessionData {

    private Employee employee;

    private Session session;

    public SessionData() {
    }

    public SessionData(Employee employee, Session session) {
        this.employee = employee;
        this.session = session;
    }

    /**
     * Получить текущего прользователя
     * @return текущий пользователь
     */
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Получить текущую сесию пользователя
     * @return текщая сесия
     */
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
