package ru.softlab.efr.services.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.auth.model.Segment;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.SegmentRepository;

import java.util.List;

/**
 * Сервис для работы со справочником сегментов
 */
@Service
public class SegmentService{


    @Value ("${Segment.NotFound}")
    private String segmentNotFound;

    @Autowired
    private SegmentRepository segmentRepository;

    /**
     * Получить все сегементы
     *
     * @return список сегментов
     */
    @Transactional (readOnly = true)
    public List<Segment> findAll() {
        return segmentRepository.findAll();
    }

    /**
     * Найти сегмент по идентификатору.
     *
     * @param id идентификатор сегмента
     * @return сегмент
     */
    @Transactional (readOnly = true)
    public Segment findById(long id) {
        return segmentRepository.findOne(id);
    }

    /**
     * Найти сегмент по коду.
     *
     * @param code код сегмента
     * @return сегмент
     */
    @Transactional (readOnly = true)
    public Segment findByCode(String code) {
        return segmentRepository.findSegmentByCode(code);
    }

    /**
     * Найти сегмент по наименованию
     *
     * @param name имя сегмента
     * @return сегмент
     */
    @Transactional (readOnly = true)
    public Segment findByName(String name) {
        return segmentRepository.findSegmentByName(name);
    }



}
