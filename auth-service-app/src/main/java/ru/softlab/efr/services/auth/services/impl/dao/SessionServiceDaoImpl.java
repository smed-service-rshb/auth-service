package ru.softlab.efr.services.auth.services.impl.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.softlab.efr.services.auth.model.Session;
import ru.softlab.efr.services.auth.services.SessionStoreService;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.RoleRepository;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.SessionRepository;

/**
 * Реализация серсиса работы с БД в разрезе сессий
 *
 * @author niculichev
 * @since 22.04.2017
 */
@Service
public class SessionServiceDaoImpl implements SessionStoreService {
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public Session getByUID(String uid) {
        return sessionRepository.findOne(uid);
    }

    @Override
    public String save(Session session) {
        sessionRepository.saveAndFlush(session);
        return session.getUid();
    }

    @Override
    public int close(String uid) {
        return sessionRepository.close(uid);
    }
}
