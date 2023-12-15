package ru.softlab.efr.services.auth.services.impl.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.auth.model.Role;
import ru.softlab.efr.services.auth.services.RoleStoreService;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Реализация серсиса работы с БД в разрезе ролей
 *
 * @author niculichev
 * @since 22.04.2017
 */
@Service
public class RoleServiceDaoImpl implements RoleStoreService{

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getAllByNames(Collection<String> names) {
        List<Role> res = new ArrayList<>(names.size());
        for(String name: names){
            Role role = getByName(name);
            if(role != null){
                res.add(role);
            }
        }
        return res;
    }

    @Override
    public Long save(Role role) {
        roleRepository.saveAndFlush(role);
        return role.getId();
    }

    @Override
    @Transactional
    public int update(Role role) {
        roleRepository.saveAndFlush(role);
        return 1;
    }

    @Override
    public Role getByName(String name) {
        return roleRepository.getByName(name);
    }

    @Override
    public Role getById(Long id) {
        return roleRepository.findOne(id);
    }

    @Override
    public int deleteById(Long id) {
        try{
            roleRepository.delete(id);
            return 1;
        } catch (EmptyResultDataAccessException e){
            return 0;
        }
    }
}
