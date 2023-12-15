package ru.softlab.efr.services.auth.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.softlab.efr.services.auth.exchange.model.*;
import ru.softlab.efr.services.auth.model.Role;
import ru.softlab.efr.services.auth.services.RoleStoreService;
import ru.softlab.efr.services.authorization.annotations.HasPermission;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.auth.config.Permissions.*;

/**
 * Контроллер обработки запросов, связанных с ролями
 *
 * @author niculichev
 * @since 12.04.2017
 */
@Validated
@RestController
public class RoleController implements RolesApi {
    private static final Logger LOGGER = Logger.getLogger(RoleController.class);

    @Autowired
    @Qualifier("roleServiceDaoImpl")
    private RoleStoreService roleStoreService;


    @HasPermission(CREATE_ROLE)
    @Override
    public ResponseEntity<EntityIdRs> addRole(@Valid @RequestBody CreateRoleRq roleData) throws Exception {
        Role role = buildEntityInfo(roleData);
        roleStoreService.save(role);
        return ResponseEntity.ok().body(new EntityIdRs(role.getId()));
    }

    @HasPermission(DELETE_ROLE)
    @Override
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) throws Exception {
        if (roleStoreService.deleteById(id) == 0) {
            LOGGER.error(String.format("Роль не найдена по идентификатору %s.", id));
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(null);
    }

    @HasPermission(GET_ROLE)
    @Override
    public ResponseEntity<GetRoleRs> getRole(@PathVariable Long id) throws Exception {
        Role role = roleStoreService.getById(id);
        if (role == null) {
            LOGGER.error(String.format("Роль не найдена по идентификатору %s.", id));
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(buildResInfo(role));
    }

    @HasPermission(GET_ROLES)
    @Override
    public ResponseEntity<GetRolesRs> getRoles() throws Exception {
        return getRolesInner();
    }

    @Override
    public ResponseEntity<GetRolesRs> getRolesWithOutPermissions() throws Exception {
        return getRolesInner();
    }

    private ResponseEntity<GetRolesRs> getRolesInner() {
        List<Role> roles = roleStoreService.getAll();
        GetRolesRs rs = new GetRolesRs();
        rs.setRoles(new ArrayList<>());
        for (Role role : roles) {
            rs.getRoles().add(new RoleData(role.getId(), role.getName(), role.getDescription(), role.getRights().parallelStream().map(m->m.toString()).collect(Collectors.toList())));
        }
        return ResponseEntity.ok().body(rs);
    }

    @HasPermission(UPDATE_ROLE)
    @Override
    public ResponseEntity<Void> updateRole(@PathVariable Long id, @Valid @RequestBody CreateRoleRq roleData) throws Exception {
        Role role = roleStoreService.getById(id);
        if (role == null) {
            return ResponseEntity.notFound().build();
        }

        updateEntityInfo(role, roleData);
        roleStoreService.update(role);

        return ResponseEntity.ok().build();
    }

    private Role buildEntityInfo(CreateRoleRq createRoleRq) {
        Role role = new Role();
        role.setName(createRoleRq.getName());
        role.setDescription(createRoleRq.getDesc());
        role.setRights(
                createRoleRq.getRights().parallelStream()
                        .map(ru.softlab.efr.services.auth.Right::valueOf)
                        .collect(Collectors.toList()));
        return role;
    }

    private void updateEntityInfo(Role role, CreateRoleRq createRoleRq) {
        role.setName(createRoleRq.getName());
        role.setDescription(createRoleRq.getDesc());
        role.setRights(createRoleRq.getRights().parallelStream()
                .map(ru.softlab.efr.services.auth.Right::valueOf)
                .collect(Collectors.toList()));
    }

    private GetRoleRs buildResInfo(Role role) {
        GetRoleRs rs = new GetRoleRs();
        Utils.mapSimilarObjects(role, rs);
        rs.setDesc(role.getDescription());
        rs.setRights(role.getRights().parallelStream().map(m->m.toString()).collect(Collectors.toList()));
        return rs;
    }

}
