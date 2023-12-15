package ru.softlab.efr.services.auth.services.impl.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.services.UserStoreService;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.UserRepository;

import java.util.List;

/**
 * Реализация серсиса работы с БД в разрезе ролей
 *
 * @author niculichev
 * @since 22.04.2017
 */
@Service
public class UserServiceDaoImpl implements UserStoreService {

    private static final Sort USER_LIST_SORT_BY_NAME = new Sort(Sort.Direction.ASC, "secondName", "firstName", "middleName");

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Employee> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<Employee> getAll(Boolean deleted) {
        if (deleted != null) {
            return userRepository.findAllByDeleted(deleted, USER_LIST_SORT_BY_NAME);
        }
        return userRepository.findAll(USER_LIST_SORT_BY_NAME);
    }

    @Override
    public Employee getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Employee getByIdNotDeleted(Long id) {
        return userRepository.findByIdAndDeleted(id, false);
    }

    @Override
    public Employee getByLogin(String login) {
        return userRepository.findTopByLogin(login);
    }


}
