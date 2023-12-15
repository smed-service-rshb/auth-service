package ru.softlab.efr.services.authorization;

import ru.softlab.efr.services.auth.Right;

import java.util.List;

/**
 * Данные по текущему(в разрезе нитки) принципалу
 *
 * @author niculichev
 * @since 30.05.2017
 */
public interface PrincipalData {
    Long getId();

    String getLogin();

    String getFirstName();

    String getSecondName();

    String getMiddleName();

    List<Right> getRights();

    String getOffice();

    Long getOfficeId();

    String getBranch();

    String getPersonnelNumber();

    String getMobilePhone();

    List<String> getOffices();

    List<String> getGroups();
}
