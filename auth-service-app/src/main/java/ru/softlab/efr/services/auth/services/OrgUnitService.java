package ru.softlab.efr.services.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.auth.exceptions.OrgUnitTypeNotFoundException;
import ru.softlab.efr.services.auth.exceptions.UserOfficeNotFoundException;
import ru.softlab.efr.services.auth.model.OrgUnit;
import ru.softlab.efr.services.auth.model.OrgUnitType;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.OrgUnitRepository;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Сервис для работы с организационной штатной структурой (ОШС).
 */
@Service
public class OrgUnitService {


    private static final Sort SORT_BY_NAME = new Sort(Sort.Direction.ASC, "name");

    @Value ("${OrgUnit.type.NotFound}")
    private String orgUnitTypeNotFound;


    @Autowired
    private OrgUnitRepository orgUnitRepository;

    /**
     * Найти ОШС по идентификатору.
     *
     * @param id идентификатор ОШС
     * @return ОШС
     */
    @Transactional (readOnly = true)
    public OrgUnit findById(long id) throws UserOfficeNotFoundException {
        return orgUnitRepository.findOne(id);
    }

    /**
     * Найти ОШС по идентификатору.
     *
     * @param ids идентификатор ОШС
     * @return ОШС
     */
    @Transactional (readOnly = true)
    public OrgUnit findByIdNotStrict(List<Long> ids) throws UserOfficeNotFoundException {
        return orgUnitRepository.findTopByIdIn(ids);
    }

    /**
     * Найти ОШС по названию регионального филиала (РФ)
     * или номеру внутреннего структурного подразделения (ВСП)
     * в зависимости от переданного типа
     *
     * @param namePart Наименование регионального филиала (РФ) или номер внутреннего структурного подразделения (ВСП)
     * @param orgUnitType тип ОШС
     * @return список ОШС
     */
    @Transactional (readOnly = true)
    public List<OrgUnit> findAllByNameAndType(String namePart, Integer orgUnitType) throws OrgUnitTypeNotFoundException {
        if (isExistsOrgUnitType(orgUnitType)) {
            return orgUnitRepository.findOrgUnitsByNameContainsAndType(namePart, OrgUnitType.values()[orgUnitType]);
        } else {
            throw new OrgUnitTypeNotFoundException(String.format(orgUnitTypeNotFound,orgUnitType));
        }
    }

    /**
     * Найти ОШС по номеру внутреннего структурного подразделения (ВСП).
     *
     * @param name Номер внутреннего структурного подразделения (ВСП)
     * @return ОШС
     */
    @Transactional (readOnly = true)
    public List<OrgUnit> findAllByOffice(String name, Integer orgUnitType) throws OrgUnitTypeNotFoundException {
        if (isExistsOrgUnitType(orgUnitType)) {
            return orgUnitRepository.findOrgUnitsByNameAndType(name, OrgUnitType.values()[orgUnitType]);
        } else {
            throw new OrgUnitTypeNotFoundException(orgUnitTypeNotFound);
        }
    }

    /**
     * Найти дочерние ОШС.
     *
     * @param id идентификатор ОШС
     * @return список ОШС
     */
    @Transactional (readOnly = true)
    public List<OrgUnit> findChildOUById(long id) {
        return orgUnitRepository.findOrgUnitsByParent(id);
    }

    /**
     * Найти дочерние ОШС.
     *
     * @param id идентификатор ОШС
     * @return список ОШС
     */
    @Transactional (readOnly = true)
    public Page<OrgUnit> findChildOUById(long id, Pageable pageable) {
        return orgUnitRepository.findOrgUnitsByParent(id, pageable);
    }

    /**
     * Найти ОШС по типу.
     *
     * @param orgUnitType Тип ОШС. Допустимые значения: 0(РФ), 1(ВСП)
     * @return список ОШС
     */
    @Transactional (readOnly = true)
    public List<OrgUnit> findOUByType(Integer orgUnitType) throws OrgUnitTypeNotFoundException {
        if (isExistsOrgUnitType(orgUnitType)) {
            return orgUnitRepository.findOrgUnitsByType(OrgUnitType.values()[orgUnitType]);
        } else {
            throw new OrgUnitTypeNotFoundException(orgUnitTypeNotFound);
        }
    }

    /**
     * Найти ОШС по типу.
     *
     * @param orgUnitType Тип ОШС. Допустимые значения: РФ, ВСП
     * @param pageable Параметры постраничного вывода
     * @return список ОШС
     */
    @Transactional (readOnly = true)
    public Page<OrgUnit> findOUByType(Integer orgUnitType, Pageable pageable) throws OrgUnitTypeNotFoundException {
        if (isExistsOrgUnitType(orgUnitType)) {
            return orgUnitRepository.findOrgUnitsByType(OrgUnitType.values()[orgUnitType], pageable);
        } else {
            throw new OrgUnitTypeNotFoundException(orgUnitTypeNotFound);
        }
    }

    /**
     * Найти ОШС определённого города.
     *
     * @param city идентификатор ОШС
     * @return список ОШС
     */
    @Transactional (readOnly = true)
    public Page<OrgUnit> findOUByCity(String city, Pageable pageable) {
        return orgUnitRepository.findOrgUnitsByCity(city, pageable);
    }

    /**
     * Получить полный список ОШС в соответствии с условиями разбивки результата на страницы.
     *
     * @param pageable условия разбивки результата на страницы
     * @return список ОШС
     */
    @Transactional (readOnly = true)
    public Page<OrgUnit> findAll(Pageable pageable) {
        return orgUnitRepository.findAll(pageable);
    }

    /**
     * Получить полный список ОШС без условий разбивки результата на страницы.
     *
     * @return список ОШС
     */
    @Transactional (readOnly = true)
    @Cacheable (value = "getOrgUnitList")
    public List<OrgUnit> findAll() {
        return orgUnitRepository.findAll(SORT_BY_NAME);
    }

    /**
     * Обновление информации по ОШС.
     *
     * @param orgUnit ОШС
     * @return обновлённая ОШС
     */
    @Transactional (rollbackFor = EntityNotFoundException.class)
    public OrgUnit update(OrgUnit orgUnit) {
        OrgUnit updatedEntity = orgUnitRepository.findOne(orgUnit.getId());
        if (updatedEntity == null) {
            throw new EntityNotFoundException();
        }

        return orgUnitRepository.save(orgUnit);
    }

    /**
     * Создать ОШС.
     *
     * @param orgUnit ОШС
     * @return ОШС
     */
    @Transactional (rollbackFor = EntityExistsException.class)
    public OrgUnit create(OrgUnit orgUnit) {
        if (orgUnit.getId() != null && orgUnitRepository.findOne(orgUnit.getId()) != null) {
            throw new EntityExistsException();
        }
        return orgUnitRepository.save(orgUnit);
    }

    /**
     * Существует ли указанный id orgUnitType, не выходит ли он за диапазон допустимых значений
     *
     * @param type orgUnitType
     * @return boolean
     */
    private boolean isExistsOrgUnitType(Integer type) {
       return (type > -1 && type < OrgUnitType.values().length );
    }

}
