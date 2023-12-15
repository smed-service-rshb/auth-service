package ru.softlab.efr.services.test.auth.integration;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.softlab.efr.services.auth.EmployeesManageAuthServiceClient;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.UserData;
import ru.softlab.efr.services.auth.exceptions.DataValidationException;
import ru.softlab.efr.services.auth.exceptions.EntityNotFoundException;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.test.auth.config.TestApplicationConfig;

import java.util.List;

import static ru.softlab.efr.services.test.auth.unit.EmployeeControllerTest.assertEqualEmployees;

/**
 * @author niculichev
 * @ created 30.04.2017
 * @ $Author$
 * @ $Revision$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@Ignore
public class EmployeeIntegrationTest extends IntegrationTestBase{
    private static final String EMPLOYEE_OFFICE = EmployeeIntegrationTestData.OFFICE_FOR_SEARCH;
    private static final Employee EMPLOYEE_FOR_SEARCH = EmployeeIntegrationTestData.EMPLOYEE_FOR_SEARCH;
    private static final List<Right> EMPLOYEE_FOR_SEARCH_RIGHTS = EmployeeIntegrationTestData.EMPLOYEE_FOR_SEARCH_RIGHTS;

    @Autowired
    private EmployeesManageAuthServiceClient employeesClient;

    // *************************** ТЕСТЫ *****************************************


    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentExc_When_NullOffice() throws Exception {
        employeesClient.getEmployees(null, TIMEOUT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_ThrowIllegalArgumentExc_When_EmptyOffice() throws Exception {
        employeesClient.getEmployees("", TIMEOUT);
    }

    @Test(expected = DataValidationException.class)
    public void should_ThrowDataValidationExc_When_InvalidOfficeFormat() throws Exception {
        employeesClient.getEmployees("asfd", TIMEOUT);
    }

    @Test(expected = EntityNotFoundException.class)
    public void should_ThrowEntityNotFoundExc_When_InvalidOfficeKey() throws Exception {
        employeesClient.getEmployees("9999", TIMEOUT);
    }

    @Test
    public void should_getEmployee_when_getEmployee() throws Exception {
        UserData actualData = employeesClient.getEmployee(EMPLOYEE_FOR_SEARCH.getPersonnelNumber(), EMPLOYEE_FOR_SEARCH.getOrgUnits().iterator().next().getName(), TIMEOUT);
        assertEqualEmployees(actualData, EMPLOYEE_FOR_SEARCH, EMPLOYEE_FOR_SEARCH_RIGHTS);
    }
}
