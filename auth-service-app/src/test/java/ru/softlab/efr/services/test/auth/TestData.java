package ru.softlab.efr.services.test.auth;

import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;

import java.util.Arrays;

public class TestData {
    public static final PrincipalDataImpl USER_PRINCIPAL_DATA;
    public static final PrincipalDataImpl CHIEF_ADMIN_PRINCIPAL_DATA;
    public static final PrincipalDataImpl CLIENT_PRINCIPAL_DATA;
    static {
        USER_PRINCIPAL_DATA = new PrincipalDataImpl();
        USER_PRINCIPAL_DATA.setId(3L);
        USER_PRINCIPAL_DATA.setFirstName("Фёдор");
        USER_PRINCIPAL_DATA.setMiddleName("Фёдорович");
        USER_PRINCIPAL_DATA.setSecondName("Фёдоров");
        USER_PRINCIPAL_DATA.setRights(Arrays.asList(
                Right.VIEW_CLIENT,
                Right.VIEW_CLIENT_CONTRACTS,
                Right.CREATE_CONTRACT,
                Right.EDIT_CONTRACT,
                Right.DELETE_CONTRACT,
                Right.VIEW_CONTRACT_LIST_OWNER,
                Right.VIEW_CONTRACT_REPORT_OWNER,
                Right.VIEW_MOTIVATION_PROGRAMS,
                Right.TAKE_PART_IN_MOTIVATION_PROGRAMS));
        USER_PRINCIPAL_DATA.setBranch("ДРБ");
        USER_PRINCIPAL_DATA.setOffice("0001");
        USER_PRINCIPAL_DATA.setPersonnelNumber("1-009");

        CHIEF_ADMIN_PRINCIPAL_DATA = new PrincipalDataImpl();
        CHIEF_ADMIN_PRINCIPAL_DATA.setId(1L);
        CHIEF_ADMIN_PRINCIPAL_DATA.setFirstName("Иван");
        CHIEF_ADMIN_PRINCIPAL_DATA.setMiddleName("Иванович");
        CHIEF_ADMIN_PRINCIPAL_DATA.setSecondName("Иванов");
        CHIEF_ADMIN_PRINCIPAL_DATA.setRights(Arrays.asList(
                Right.EDIT_ROLES,
                Right.VIEW_ROLES,
                Right.SET_ADMIN_ROLE,
                Right.EDIT_USERS,
                Right.EDIT_PRODUCT_SETTINGS,
                Right.EDIT_UNDERWRITING_COEFFICIENTS,
                Right.EDIT_UNDERWRITING_SUMS,
                Right.VIEW_CLIENT,
                Right.VIEW_CLIENT_CONTRACTS,
                Right.CREATE_CONTRACT,
                Right.DELETE_CONTRACT,
                Right.EDIT_CONTRACT,
                Right.VIEW_CONTRACT_LIST_ALL,
                Right.VIEW_CONTRACT_LIST_RF_VSP,
                Right.VIEW_CONTRACT_LIST_VSP,
                Right.VIEW_CONTRACT_LIST_OWNER,
                Right.VIEW_CONTRACT_REPORT_ALL,
                Right.VIEW_CONTRACT_REPORT_RF_VSP,
                Right.VIEW_CONTRACT_REPORT_VSP,
                Right.VIEW_CONTRACT_REPORT_OWNER,
                Right.VIEW_CONTRACT_REQUIRED_UNDERWRITING,
                Right.CHANGE_STATE_CONTRACT_REQUIRED_UNDERWRITING,
                Right.EDIT_CONTRACT_REQUIRED_UNDERWRITING,
                Right.CONTROL_MOTIVATION_PROGRAMS,
                Right.MOTIVATION_REPORT_CREATE));
        CHIEF_ADMIN_PRINCIPAL_DATA.setBranch("ДРБ");
        CHIEF_ADMIN_PRINCIPAL_DATA.setOffice("0001");
        CHIEF_ADMIN_PRINCIPAL_DATA.setPersonnelNumber("1-001");
        //Клиент
        CLIENT_PRINCIPAL_DATA = new PrincipalDataImpl();
        CLIENT_PRINCIPAL_DATA.setId(9L);
        CLIENT_PRINCIPAL_DATA.setFirstName("Клиент");
        CLIENT_PRINCIPAL_DATA.setMiddleName("Тестовый");
        CLIENT_PRINCIPAL_DATA.setSecondName("Лк");
        CLIENT_PRINCIPAL_DATA.setMobilePhone("+79110000010");
        CLIENT_PRINCIPAL_DATA.setRights(Arrays.asList(Right.CLIENT_VIEW_CONTRACT, Right.CLIENT_VIEW_CONTRACTS_LIST));

    }
}
