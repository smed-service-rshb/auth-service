package ru.softlab.efr.services.test.auth.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.UserData;
import ru.softlab.efr.services.auth.controllers.EmployeeController;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.services.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.softlab.efr.services.test.auth.integration.EmployeeIntegrationTestData.CORRECT_EMPLOYEE;

/**
 * @author niculichev
 * @ created 30.04.2017
 * @ $Author$
 * @ $Revision$
 */
@RunWith(JUnit4.class)
@Ignore
public class EmployeeControllerTest {
    private static final List<Right> CORRECT_EMPLOYEE_RIGHTS = Collections.singletonList(Right.ACCESS_CLIENTS_EXCEPT_MAIN_OFFICE);

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private SegmentService segmentService;

    @Mock
    private OrgUnitService orgUnitService;

    @Mock
    private AuthenticateService authenticateService;

    @Mock
    private RoleStoreService roleService;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        when(employeeService.findByOffice(0000L)).thenReturn(Collections.emptyList());
//        when(employeeService.findByIdAndOffice(CORRECT_EMPLOYEE.getPersonnelNumber(), CORRECT_EMPLOYEE.getOrgUnit().getId())).thenReturn(CORRECT_EMPLOYEE);
        when(employeeService.findAll()).thenReturn(Collections.emptyList());
        when(employeeService.findById(CORRECT_EMPLOYEE.getId())).thenReturn(CORRECT_EMPLOYEE);
        when(employeeService.findByLogin(CORRECT_EMPLOYEE.getLogin())).thenReturn(CORRECT_EMPLOYEE);
        when(authenticateService.getRights(CORRECT_EMPLOYEE)).thenReturn(CORRECT_EMPLOYEE_RIGHTS);
        when(employeeService.create(any())).thenReturn(CORRECT_EMPLOYEE);
        when(roleService.getByName(CORRECT_EMPLOYEE.getRoles().get(0).getName())).thenReturn(CORRECT_EMPLOYEE.getRoles().get(0));
        when(orgUnitService.findById(CORRECT_EMPLOYEE.getOrgUnits().iterator().next().getId())).thenReturn(CORRECT_EMPLOYEE.getOrgUnits().iterator().next());
        when(segmentService.findById(CORRECT_EMPLOYEE.getSegment().getId())).thenReturn(CORRECT_EMPLOYEE.getSegment());
    }

    private ResultActions getEmployees() throws Exception {
        return mockMvc.perform(get("/auth/v1/employees"));
    }
    private ResultActions getEmployees(String office) throws Exception {
        return mockMvc.perform(
                get("/auth/v1/employees")
                        .param("office", office)
        );
    }


    private ResultActions getEmployeeById(Long id) throws Exception {
        return mockMvc.perform(get(String.format("/auth/v1/employees/%s", id)));
    }

    // *************************** ТЕСТЫ *****************************************

    @Test
    public void getEmployees_should_OK_when_CorrectOffice() throws Exception {
        getEmployees("7800")
                .andExpect(status().isOk());
    }

    @Test
    public void getEmployeeById_should_OK_When_CorrectId() throws Exception {
        String employeeResponse = getEmployeeById(CORRECT_EMPLOYEE.getId())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserData employee = new ObjectMapper().readerFor(UserData.class).readValue(employeeResponse);
        assertEqualEmployees(employee, CORRECT_EMPLOYEE, CORRECT_EMPLOYEE_RIGHTS);
    }

    @Test
    public void getEmployeeById_should_Empty_When_WrongId() throws Exception {
        getEmployeeById(CORRECT_EMPLOYEE.getId() * 7).andExpect(status().isNotFound());
    }

    public static void assertEqualEmployees(UserData actual, Employee expected, List<Right> expectedRights) {
        assertNotNull(actual);
        assertEquals(actual.getLogin(), expected.getLogin());
        assertEquals(actual.getFirstName(), expected.getFirstName());
        assertEquals(actual.getSecondName(), expected.getSecondName());
        assertEquals(actual.getMiddleName(), expected.getMiddleName());
        assertEquals(actual.getMobilePhone(), expected.getMobilePhone());
        assertEquals(actual.getEmail(), expected.getEmail());
        assertEquals(actual.getPosition(), expected.getPosition());
//        assertEquals(actual.getOffice(), expected.getOrgUnit().getName());
//        assertEquals(actual.getBranch(), expected.getOrgUnit().getParent().getName());
        assertEquals(actual.getPersonnelNumber(), expected.getPersonnelNumber());
        assertEquals(actual.getRights(), expectedRights);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
