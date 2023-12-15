package ru.softlab.efr.services.test.auth.integration;

import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.OrgUnit;
import ru.softlab.efr.services.auth.model.Role;
import ru.softlab.efr.services.auth.model.Segment;

import java.util.*;

/**
 * Сервис AD для домена RF
 *
 * @author akrenev
 * @since 13.03.2018
 */
public class EmployeeIntegrationTestData {
    public static final Employee CORRECT_EMPLOYEE;
    public static final String USERS_CORRECT_PASSWORD = "ia4uV1EeKait";
    static final String USER_EMPTY_ROLE_LOGIN = "it-empty-role@go.rshbank.ru";
    static final String USER_WRONG_ROLE_LOGIN = "it-wrong-role@go.rshbank.ru";
    static final String USER_WRONG_DEPARTMENT_LOGIN = "it-wrong-department@go.rshbank.ru";
    static final String USER_BLOCKED_LOGIN = "it-blocked@go.rshbank.ru";

    static final String DOMAIN_USER_WRONG_LOGIN = "ad-integration-test-unknown@go.rshbank.ru";

    static final String USERS_WRONG_PASSWORD = "12345";

    static final String OFFICE_FOR_SEARCH = "0002";

    private static final String USER_WRONG_LOGIN = DOMAIN_USER_WRONG_LOGIN;

    private static final Employee SINGLE_ROLE_EMPLOYEE;
    private static final Employee MULTI_ROLE_EMPLOYEE;
    public static final Employee EMPLOYEE_FOR_SEARCH;
    static final List<Right> EMPLOYEE_FOR_SEARCH_RIGHTS = new LinkedList<>();

    static {
        SINGLE_ROLE_EMPLOYEE = new Employee();
        SINGLE_ROLE_EMPLOYEE.setLogin("it-single-role@go.rshbank.ru");
        SINGLE_ROLE_EMPLOYEE.setFirstName("Алексей");
        SINGLE_ROLE_EMPLOYEE.setSecondName("Комаров");
        SINGLE_ROLE_EMPLOYEE.setMiddleName("Сергеевич");
        SINGLE_ROLE_EMPLOYEE.setMobilePhone("+79638527442");
        SINGLE_ROLE_EMPLOYEE.setEmail("it-single-role@go.rshbank.mail.ru");
        SINGLE_ROLE_EMPLOYEE.setPosition("BIG-BOSS");
        //SINGLE_ROLE_EMPLOYEE.setOffice("0003");
        //SINGLE_ROLE_EMPLOYEE.setBranch("0000");
        SINGLE_ROLE_EMPLOYEE.setPersonnelNumber("N79638527442");
        SINGLE_ROLE_EMPLOYEE.setRoles(roles("Менеджер ГО"));

        MULTI_ROLE_EMPLOYEE = new Employee();
        MULTI_ROLE_EMPLOYEE.setLogin("it-multi-role@go.rshbank.ru");
        MULTI_ROLE_EMPLOYEE.setFirstName("Петр");
        MULTI_ROLE_EMPLOYEE.setSecondName("Петров");
        MULTI_ROLE_EMPLOYEE.setMobilePhone("+79638527441");
        MULTI_ROLE_EMPLOYEE.setEmail("it-multi-role@go.rshbank.mail.ru");
        MULTI_ROLE_EMPLOYEE.setPosition("BOSS");
        //MULTI_ROLE_EMPLOYEE.setOffice("0000");
        //MULTI_ROLE_EMPLOYEE.setBranch("0000");
        MULTI_ROLE_EMPLOYEE.setPersonnelNumber("N79638527441");
        MULTI_ROLE_EMPLOYEE.setRoles(roles("Менеджер", "Операционист зарплатных проектов РСХБ"));

        CORRECT_EMPLOYEE = new Employee();
        CORRECT_EMPLOYEE.setId(13L);
        CORRECT_EMPLOYEE.setLogin("it-search-first@go.rshbank.ru");
        CORRECT_EMPLOYEE.setFirstName("Сидор");
        CORRECT_EMPLOYEE.setSecondName("Сидоров");
        CORRECT_EMPLOYEE.setMiddleName("Сидорович");
        CORRECT_EMPLOYEE.setMobilePhone("+79638527443");
        CORRECT_EMPLOYEE.setEmail("it-search-first@go.rshbank.mail.ru");
        CORRECT_EMPLOYEE.setPosition("slave");
        OrgUnit orgUnit = new OrgUnit();
        orgUnit.setId(2000001L);
        Set<OrgUnit> orgUnits = new HashSet<>();
        orgUnits.add(orgUnit);
        CORRECT_EMPLOYEE.setOrgUnits(orgUnits);
        Segment segment = new Segment();
        segment.setId(1L);
        CORRECT_EMPLOYEE.setSegment(segment);
        CORRECT_EMPLOYEE.setPersonnelNumber("79638527443");
        CORRECT_EMPLOYEE.setRoles(roles(2L));

/*
        insert into users (id, login, passwordHash, firstName, middleName, secondName, mobilePhone,
                email, position, personnelNumber, segment, changePassword, orgunit, deleted)
        values (1, 'chief-admin', '356a192b7913b04c54574d18c28d46e6395428ab', 'Иван', 'Иванович', 'Иванов', '+79110000001',
                'ivanov@example.org', 'Главный администратор системы', '1-001', 1, false, 2000001, false);
*/

        EMPLOYEE_FOR_SEARCH = new Employee();
        EMPLOYEE_FOR_SEARCH.setId(1L);
        EMPLOYEE_FOR_SEARCH.setLogin("chief-admin");
        EMPLOYEE_FOR_SEARCH.setFirstName("Иван");
        EMPLOYEE_FOR_SEARCH.setSecondName("Иванов");
        EMPLOYEE_FOR_SEARCH.setMiddleName("Иванович");
        EMPLOYEE_FOR_SEARCH.setMobilePhone("+79110000001");
        EMPLOYEE_FOR_SEARCH.setEmail("ivanov@example.org");
        EMPLOYEE_FOR_SEARCH.setPosition("Главный администратор системы");
        OrgUnit orgUnit2 = new OrgUnit();
        orgUnit2.setId(2000001L);
        Set<OrgUnit> orgUnits2 = new HashSet<>();
        orgUnits2.add(orgUnit2);
        EMPLOYEE_FOR_SEARCH.setOrgUnits(orgUnits2);
        Segment segment2 = new Segment();
        segment2.setId(1L);
        EMPLOYEE_FOR_SEARCH.setSegment(segment2);
        EMPLOYEE_FOR_SEARCH.setPersonnelNumber("1-001");

        EMPLOYEE_FOR_SEARCH.setRoles(roles(1L));
        EMPLOYEE_FOR_SEARCH_RIGHTS.add(Right.ACCESS_CLIENTS_EXCEPT_MAIN_OFFICE);
        EMPLOYEE_FOR_SEARCH_RIGHTS.add(Right.ACCESS_CLIENTS_MAIN_OFFICE);
    }

    private static List<Role> roles(String... keys) {
        List<Role> roles = new ArrayList<Role>(keys.length);
        for (String key : keys) {
            Role role = new Role();
            role.setName(key);
            roles.add(role);
        }
        return roles;
    }

    private static List<Role> roles(Long... keys) {
        List<Role> roles = new ArrayList<Role>(keys.length);
        for (Long key : keys) {
            Role role = new Role();
            role.setId(key);
            roles.add(role);
        }
        return roles;
    }
}
