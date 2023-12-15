package ru.softlab.efr.services.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.auth.model.EmployeeGroup;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.EmployeeGroupRepository;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
public class EmployeeGroupService {

    @Autowired
    private EmployeeGroupRepository repository;

    /**
     * Сохранить сущность справочника групп
     *
     * @param employeeGroup сущность справочника
     * @return сохраненная сущность
     */
    @Transactional
    public EmployeeGroup createGroup(EmployeeGroup employeeGroup) {
        EmployeeGroup group = repository.findByCode(employeeGroup.getCode());
        if (group != null) {
            throw new EntityExistsException();
        }
        return repository.save(employeeGroup);
    }

    /**
     * Найти все записи справочника
     *
     * @return список записей справочника
     */
    @Transactional(readOnly = true)
    public List<EmployeeGroup> findAll() {
        return repository.findAll();
    }

    /**
     * Получить страницу записей справочника
     *
     * @param pageable параметры страницы
     * @return постраничный вывод записей справочника
     */
    @Transactional(readOnly = true)
    public Page<EmployeeGroup> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Найти запись справочника групп пользователей по идентификатору
     *
     * @param id идентификатор записи
     * @return запись справочника групп пользователей
     */
    public EmployeeGroup findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Обновить запись справочника
     *
     * @param group сущность для обновления
     * @return обновленная сущность
     */
    @Transactional
    public EmployeeGroup update(EmployeeGroup group) {
        EmployeeGroup existGroup = repository.findByCode(group.getCode());

        if (existGroup != null && !(existGroup.getId().equals(group.getId()))) {
            throw new EntityExistsException();
        }
        return repository.save(group);
    }
}
