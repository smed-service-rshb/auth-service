package ru.softlab.efr.services.test.auth.integration;

import org.apache.commons.collections.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.RightData;
import ru.softlab.efr.services.auth.RoleListData;
import ru.softlab.efr.services.auth.RolesManageAuthServiceClient;
import ru.softlab.efr.services.auth.exceptions.ConstraintEntityParametersException;
import ru.softlab.efr.services.auth.exceptions.EntityNotFoundException;
import ru.softlab.efr.services.auth.exchange.*;
import ru.softlab.efr.services.auth.model.Role;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.RoleRepository;
import ru.softlab.efr.services.test.auth.config.TestApplicationConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author niculichev
 * @ created 29.04.2017
 * @ $Author$
 * @ $Revision$
 */
@SuppressWarnings("Duplicates")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@Ignore
public class RoleManageClientIntegrationTest extends IntegrationTestBase{

    private static final String NEW_ROLE_CLIENT_KEY = "it-role-new-client";
    private Role adminRole;

    private Role clientRole;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RolesManageAuthServiceClient roleClient;

    @Before
    @SuppressWarnings("Duplicates")
    public void before(){
        if(this.adminRole == null){
            this.adminRole = new Role();
            adminRole.setName("it-role-admin");
            adminRole.setDescription("it-role Администратор системы");
            adminRole.setRights(Collections.singletonList(Right.ANALYZE_OPEN_DEPOSIT_PROCESS_ERRORS));
            roleRepository.save(adminRole);
        }

        if(this.clientRole == null){
            this.clientRole = new Role();
            clientRole.setName("it-role-client");
            clientRole.setDescription("it-role Клиентская роль");
            clientRole.setRights(Collections.singletonList(Right.INSPECT_OPEN_DEPOSIT_PROCESS));
            roleRepository.save(clientRole);
        }
    }

    @After
    @SuppressWarnings("Duplicates")
    public void after(){
        roleRepository.delete(adminRole);
        roleRepository.delete(clientRole);
        Role newRole = roleRepository.getByName(NEW_ROLE_CLIENT_KEY);
        if (newRole != null) {
            roleRepository.delete(newRole);
        }
        this.adminRole = null;
        this.clientRole = null;
    }

    // *************************** ТЕСТЫ *****************************************

    @Test
    public void should_ReturnFulledRights_when_GetGetRights() throws Exception {
        GetAllRightDataRs response = roleClient.getAllRights(TIMEOUT);

        assertNotNull("Ожидается ответ на запрос всех доступных прав", response);
        assertNotNull("Ожидается список прав", response.getRights());

        Set<Right> actualRights = new HashSet<>();
        for (RightData rightData : response.getRights()) {
            assertNotNull("Ожидается внешний ключ права", rightData.getExternalId());
            assertNotNull("Ожидается описание права", rightData.getDescription());
            actualRights.add(rightData.getExternalId());
        }
        int expectedLength = Right.values().length;
        int actualLength = actualRights.size();
        assertEquals("Ожидается не пустой (" + expectedLength + ") список прав", expectedLength, actualLength);
    }

    @Test
    public void should_ReturnFulledRoles_when_GetRoles() throws Exception {
        GetRolesRs rs = roleClient.getRoles(TIMEOUT);
        assertTrue(CollectionUtils.isNotEmpty(rs.getRoles()));

        for(RoleListData roleData : rs.getRoles()){
            assertNotNull(roleData.getId());
            assertNotNull(roleData.getName());
            assertNotNull(roleData.getDesc());
        }
    }

    @Test
    public void should_ReturnFulledRole_when_GetRole() throws Exception {
        GetRoleRs rs = roleClient.getRole(this.clientRole.getId(), TIMEOUT);

        assertNotNull(rs);
        assertNotNull(rs.getName());
        assertNotNull(rs.getId());
        assertNotNull(rs.getDesc());
        assertNotNull(rs.getRights());
        assertEquals("Ожидается одно право", 1, rs.getRights().size());
    }

    @Test
    public void should_createRole_when_pushRole() throws Exception {
        SetRoleRq roleRq = new SetRoleRq();

        roleRq.setName(NEW_ROLE_CLIENT_KEY);
        roleRq.setDesc("it-role-new Клиентская роль");
        roleRq.setRights(Arrays.asList(
                Right.ANALYZE_OPEN_DEPOSIT_PROCESS_ERRORS,
                Right.ANALYZE_ACCOUNTS_TRANSFER_PROCESS_ERRORS));

        EntryIdRs rs = roleClient.postRoles(roleRq, TIMEOUT);

        Role role = roleRepository.findOne(rs.getId());
        assertNotNull(role);

        assertNotNull(role.getName());
        assertNotNull(role.getDescription());
        assertNotNull(role.getRights());
        assertEquals("Ожидается два права", 2, role.getRights().size());
    }

    @Test
    public void should_updateRole_when_putRole() throws Exception {
        SetRoleRq roleRq = new SetRoleRq();
        roleRq.setId(clientRole.getId());
        roleRq.setName(clientRole.getName());
        roleRq.setDesc(clientRole.getDescription());
        roleRq.setRights(Arrays.asList(
                ru.softlab.efr.services.auth.Right.ANALYZE_OPEN_DEPOSIT_PROCESS_ERRORS,
                ru.softlab.efr.services.auth.Right.ANALYZE_ACCOUNTS_TRANSFER_PROCESS_ERRORS));

        roleClient.putRoles(roleRq, TIMEOUT);

        Role persistentRole = roleRepository.findOne(clientRole.getId());

        assertNotNull(persistentRole);

        assertEquals(persistentRole.getName(), roleRq.getName());
        assertEquals(persistentRole.getDescription(), roleRq.getDesc());
        assertEquals(persistentRole.getId(), roleRq.getId());
        assertNotNull(persistentRole.getRights());
        assertEquals("Ожидается два права", 2, persistentRole.getRights().size());
    }

    @Test(expected = ConstraintEntityParametersException.class)
    public void should_throwConstraintEntityParametersExc_when_postDuplicateRole() throws Exception {
        SetRoleRq roleRq = new SetRoleRq();
        roleRq.setName(clientRole.getName());
        roleRq.setDesc(clientRole.getDescription());
        roleRq.setRights(Arrays.asList(
                ru.softlab.efr.services.auth.Right.ANALYZE_OPEN_DEPOSIT_PROCESS_ERRORS,
                ru.softlab.efr.services.auth.Right.ANALYZE_ACCOUNTS_TRANSFER_PROCESS_ERRORS));

        roleClient.postRoles(roleRq, TIMEOUT);
    }

    @Test(expected = ConstraintEntityParametersException.class)
    public void should_throwConstraintEntityParametersExc_when_putDuplicateRole() throws Exception {
        SetRoleRq roleRq = new SetRoleRq();
        roleRq.setId(clientRole.getId());
        roleRq.setName(adminRole.getName());
        roleRq.setDesc(adminRole.getDescription());
        roleRq.setRights(Arrays.asList(
                ru.softlab.efr.services.auth.Right.ANALYZE_OPEN_DEPOSIT_PROCESS_ERRORS,
                ru.softlab.efr.services.auth.Right.ANALYZE_ACCOUNTS_TRANSFER_PROCESS_ERRORS));

        roleClient.putRoles(roleRq, TIMEOUT);
    }

    @Test
    public void should_deleteRole_when_existId() throws Exception {
        roleClient.deleteRole(adminRole.getId(), TIMEOUT);
        assertNull(roleRepository.findOne(adminRole.getId()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void should_throwEntryNotFoundExc_when_deleteNonExistId() throws Exception {
        roleClient.deleteRole(Long.MAX_VALUE, TIMEOUT);
    }
}
