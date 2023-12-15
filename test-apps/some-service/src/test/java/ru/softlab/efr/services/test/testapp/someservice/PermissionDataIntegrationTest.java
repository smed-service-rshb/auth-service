package ru.softlab.efr.services.test.testapp.someservice;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;
import ru.softlab.efr.services.test.testapp.someservice.config.TestClientConfiguration;
import ru.softlab.efr.services.testapps.someservice.config.Permissions;
import ru.softlab.efr.test.services.auth.rest.AuthorizedRestRule;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static ru.softlab.efr.test.infrastructure.transport.rest.matchers.MockRestResultMatchers.content;
import static ru.softlab.efr.test.infrastructure.transport.rest.matchers.MockRestResultMatchers.status;

/**
 * @author niculichev
 * @ created 30.05.2017
 * @ $Author$
 * @ $Revision$
 */
@SuppressWarnings("Duplicates")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestClientConfiguration.class)
public class PermissionDataIntegrationTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    @Rule
    public AuthorizedRestRule mockRest;

    @Test
    public void should_ReturnRightPrincipleData_When_GetPrincipalData() throws Exception {
        PrincipalDataImpl expectedPrincipalData = new PrincipalDataImpl();
        expectedPrincipalData.setId(1L);
        expectedPrincipalData.setFirstName("Имя");
        expectedPrincipalData.setMiddleName("Отчество");
        expectedPrincipalData.setSecondName("Фамилия");
        expectedPrincipalData.setOffice("Офис");
        expectedPrincipalData.setBranch("Филиал");
        expectedPrincipalData.setPersonnelNumber("Табельный номер");
        expectedPrincipalData.setRights(Arrays.asList(Right.EDIT_ROLES, Right.VIEW_ROLES));

        mockRest.init(expectedPrincipalData)
                .path("/principal-data")
                .get(PrincipalDataImpl.class)
                .andExpect(status().isOk())
                .andExpect(result -> {
                    PrincipalData principalData = (PrincipalData) result.getResponse().getBody();
                    assertNotNull(principalData);
                    assertNotNull(principalData.getRights());

                    assertThat(principalData.getId(), is(expectedPrincipalData.getId()));

                    assertThat(principalData.getFirstName(), is(expectedPrincipalData.getFirstName()));
                    assertThat(principalData.getMiddleName(), is(expectedPrincipalData.getMiddleName()));
                    assertThat(principalData.getSecondName(), is(expectedPrincipalData.getSecondName()));

                    assertThat(principalData.getOffice(), is(expectedPrincipalData.getOffice()));
                    assertThat(principalData.getBranch(), is(expectedPrincipalData.getBranch()));
                    assertThat(principalData.getPersonnelNumber(), is(expectedPrincipalData.getPersonnelNumber()));

                    assertThat(principalData.getRights().size(), is(expectedPrincipalData.getRights().size()));
                    assertTrue(principalData.getRights().containsAll(expectedPrincipalData.getRights()));
                });
    }


    @Test
    public void should_Success_When_ThereAreRights() throws Exception {
        get("/test-message", String.class, HttpStatus.OK, Right.VIEW_ROLES, Right.EDIT_ROLES);
    }

    @Test
    public void should_Success_When_OrRights() throws Exception {
        get("/test-or-permission", String.class, HttpStatus.OK, Right.EDIT_ROLES);
    }

    @Test
    public void should_Success_When_AndRights() throws Exception {
        get("/test-and-permission", String.class, HttpStatus.OK, Right.VIEW_ROLES, Right.EDIT_ROLES);
    }

    @Test
    public void should_Fail_When_AndRights() throws Exception {
        get("/test-and-permission", String.class, HttpStatus.FORBIDDEN, Right.EDIT_ROLES);
    }

    @Test
    public void should_Success_When_ExistsRights() throws Exception {
        get("/test-hasRight", String.class, HttpStatus.OK, Right.EDIT_ROLES);
    }

    @Test
    public void should_Fail_When_NotExistsRights() throws Exception {
        get("/test-hasRight", String.class, HttpStatus.FORBIDDEN, Right.VIEW_ROLES);
    }

    @Test
    public void should_ReturnTrue_When_RightPerm() throws Exception {
        Boolean res = get("/imply/" + Permissions.PERMISSION1, Boolean.class, HttpStatus.OK, Right.EDIT_ROLES);
        assertTrue(res);
    }

    @Test
    public void should_ReturnFalse_When_NotExistPerm() throws Exception {
        Boolean res = mockRest.init()
                .path("/imply/" + "sdfsdfsdfsdf")
                .get(Boolean.class)
                .andExpectSafe(status(Boolean.class).is(HttpStatus.OK))
                .andReturnBody();
        assertFalse(res);
    }

    @Test
    public void should_ReturnFalse_When_NotRecordOfPerm() throws Exception {
        Boolean res = get("/imply/" + Permissions.PERMISSION3, Boolean.class, HttpStatus.OK, Right.VIEW_ROLES);
        assertFalse(res);
    }

    @Test
    public void should_SecurityContextReturnTrue_When_ExistsRight() throws Exception {
        Boolean res = get("/imply-right/" + Right.EDIT_ROLES, Boolean.class, HttpStatus.OK, Right.EDIT_ROLES);
        assertTrue(res);
    }

    @Test
    public void should_SecurityContextReturnFalse_When_NotExistsRight() throws Exception {
        Boolean res = get("/imply-right/" + Right.ANALYZE_OPEN_DEPOSIT_PROCESS_ERRORS, Boolean.class, HttpStatus.OK, Right.VIEW_ROLES);
        assertFalse(res);
    }

    @Test
    public void testSuperClassWithAllowedRole_success() throws Exception {
        String message = "sdfsdfsdf";
        String response = get("/ApiController/" + message, String.class, HttpStatus.OK, Right.EDIT_ROLES);
        assertThat(response, is(message));
    }

    @Test
    public void testSuperClassWithDisallowedRole_forbidden() throws Exception {
        get("/ApiController/blahhh", String.class, HttpStatus.FORBIDDEN, Right.ANALYZE_OPEN_DEPOSIT_PROCESS_ERRORS);
    }

    @Test
    public void testServiceWithAllowedRole_success() throws Exception {
        String message = "sdfsdfsdf";
        String response = get("/service/" + message, String.class, HttpStatus.OK, Right.EDIT_ROLES);
        assertThat(response, is(message));
    }

    @Test
    public void testServiceWithDisallowedRole_forbidden() throws Exception {
        get("/service//blahhh", String.class, HttpStatus.FORBIDDEN, Right.ANALYZE_OPEN_DEPOSIT_PROCESS_ERRORS);
    }

    private <T> T get(String url, Class<T> aClass, HttpStatus expectedStatus, Right... rights) throws Exception {
        return mockRest.init(rights)
                .path(url)
                .get(aClass)
                .andExpectSafe(status(aClass).is(expectedStatus))
                .andReturnBody();
    }
}
