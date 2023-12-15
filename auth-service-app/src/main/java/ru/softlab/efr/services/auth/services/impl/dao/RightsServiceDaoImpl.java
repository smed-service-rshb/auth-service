package ru.softlab.efr.services.auth.services.impl.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.model.Right;
import ru.softlab.efr.services.auth.services.RightStoreService;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.RightsRepository;

import java.util.List;

/**
 * Сервис хранилища прав
 *
 * @author akrenev
 * @since 15.02.2018
 */
@Service
public class RightsServiceDaoImpl implements RightStoreService {

    @Autowired
    private RightsRepository repository;

    @Override
    public List<Right> getAll() {
        return repository.findAll();
    }
}
