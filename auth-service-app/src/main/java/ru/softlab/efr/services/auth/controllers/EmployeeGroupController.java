package ru.softlab.efr.services.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.config.Permissions;
import ru.softlab.efr.services.auth.exchange.model.EmployeeGroupRs;
import ru.softlab.efr.services.auth.exchange.model.GroupData;
import ru.softlab.efr.services.auth.model.EmployeeGroup;
import ru.softlab.efr.services.auth.services.EmployeeGroupService;
import ru.softlab.efr.services.authorization.annotations.HasPermission;

import java.util.stream.Collectors;

@RestController
public class EmployeeGroupController implements EmployeeGroupApi {

    @Autowired
    private EmployeeGroupService employeeGroupService;

    @Override
    @HasPermission(Permissions.EDIT_EMPLOYEE_GROUPS)
    public ResponseEntity<GroupData> createGroup(@RequestBody GroupData groupData) throws Exception {
        if (StringUtils.isEmpty(groupData.getCode())) {
            return ResponseEntity.badRequest().build();
        }
        EmployeeGroup group = employeeGroupService.createGroup(EmployeeGroupController.convert(groupData));
        groupData.setId(group.getId());
        return ResponseEntity.ok(groupData);
    }

    @Override
    public ResponseEntity<EmployeeGroupRs> getEmployeeGroupListWithOutPermissions() throws Exception {
        return ResponseEntity.ok(new EmployeeGroupRs(employeeGroupService.findAll()
                .stream()
                .map(EmployeeGroupController::convert)
                .collect(Collectors.toList())));
    }

    @Override
    @HasPermission(Permissions.EDIT_EMPLOYEE_GROUPS)
    public ResponseEntity<Page<GroupData>> getEmployeeGroupList(Pageable pageable) throws Exception {
        return ResponseEntity.ok(employeeGroupService.findAll(pageable).map(EmployeeGroupController::convert));
    }

    @Override
    @HasPermission(Permissions.EDIT_EMPLOYEE_GROUPS)
    public ResponseEntity<GroupData> getGroupDataById(@PathVariable("id") Long id) throws Exception {
        EmployeeGroup group = employeeGroupService.findById(id);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(EmployeeGroupController.convert(group));
    }

    @Override
    @HasPermission(Permissions.EDIT_EMPLOYEE_GROUPS)
    public ResponseEntity<Void> updateGroupById(@PathVariable("id") Long id, @RequestBody GroupData groupData) throws Exception {
        if (StringUtils.isEmpty(groupData.getCode())) {
            return ResponseEntity.badRequest().build();
        }
        EmployeeGroup group = employeeGroupService.findById(id);
        if (group == null) {
            return ResponseEntity.notFound().build();
        }
        group.setCode(groupData.getCode());
        group.setName(groupData.getName());
        employeeGroupService.update(group);
        return ResponseEntity.ok().build();
    }

    private static GroupData convert(EmployeeGroup employeeGroup) {
        GroupData data = new GroupData();
        data.setId(employeeGroup.getId());
        data.setCode(employeeGroup.getCode());
        data.setName(employeeGroup.getName());
        return data;
    }

    private static EmployeeGroup convert(GroupData employeeGroup) {
        EmployeeGroup group = new EmployeeGroup();
        group.setId(employeeGroup.getId());
        group.setCode(employeeGroup.getCode());
        group.setName(employeeGroup.getName());
        return group;
    }
}
