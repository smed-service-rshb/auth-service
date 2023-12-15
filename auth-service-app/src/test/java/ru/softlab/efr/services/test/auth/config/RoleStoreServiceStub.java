package ru.softlab.efr.services.test.auth.config;

import ru.softlab.efr.services.auth.model.Role;
import ru.softlab.efr.services.auth.services.RoleStoreService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Стаб сервиса ролей
 *
 * @author akrenev
 * @since 14.03.2018
 */
class RoleStoreServiceStub implements RoleStoreService {
    private final Map<String, Role> roles;

    private Long maxId = 0L;

    RoleStoreServiceStub(Collection<String> roles) {
        this.roles = roles.stream().map(roleName -> {
            Role role = new Role();
            role.setId(maxId++);
            role.setName(roleName);
            role.setDescription(roleName + " description");
            return role;
        }).collect(Collectors.toMap(Role::getName, role -> role));
    }

    @Override
    public List<Role> getAll() {
        return new ArrayList<>(roles.values());
    }

    @Override
    public List<Role> getAllByNames(Collection<String> names) {
        return names.stream().map(this::getByName).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public Long save(Role role) {
        roles.put(role.getName(), role);
        return 0L;
    }

    @Override
    public int update(Role role) {
        roles.put(role.getName(), role);
        return 1;
    }

    @Override
    public Role getByName(String name) {
        return roles.get(name);
    }

    @Override
    public Role getById(Long id) {
        return roles.values().stream().filter(role -> role.getId().equals(id)).collect(Collectors.toList()).get(0);
    }

    @Override
    public int deleteById(Long id) {
        Role role = getById(id);
        return roles.remove(role.getName()) == null ? 0 : 1;
    }
}
