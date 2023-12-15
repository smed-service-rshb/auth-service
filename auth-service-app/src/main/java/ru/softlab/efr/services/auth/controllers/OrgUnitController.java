package ru.softlab.efr.services.auth.controllers;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.exceptions.OrgUnitTypeNotFoundException;
import ru.softlab.efr.services.auth.exceptions.UserOfficeNotFoundException;
import ru.softlab.efr.services.auth.exchange.model.*;
import ru.softlab.efr.services.auth.model.OrgUnit;
import ru.softlab.efr.services.auth.model.OrgUnitType;
import ru.softlab.efr.services.auth.services.OrgUnitService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.softlab.efr.services.auth.controllers.Utils.buildErrorRes;

/**
 * Контроллер обработки запросов, связанных с орг. структурой
 */
@Validated
@RestController
@PropertySource("classpath:ValidationMessages.properties")
public class OrgUnitController implements OrgUnitApi{

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class);


    @Value ("${OrgUnit.NotFound}")
    private String orgUnitNotFound;

    private static final String ORGUNIT_TYPE_FIELD_NAME = "type";


    @Autowired
    private OrgUnitService orgUnitService;

    @Override
    public ResponseEntity<OfficeData> getOrgUnit(@PathVariable("id") Long id) throws Exception {
        OrgUnit orgUnit = orgUnitService.findById(id);
        if (orgUnit == null) {
            throw new UserOfficeNotFoundException(String.format(orgUnitNotFound, id.toString()));
        }
        return ResponseEntity.ok().body(OrgUnitController.mapOrgUnit2Office(orgUnit));
    }

    @Override
    //@GetMapping(path = "/orgunits/children/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrgUnitsData> listChildOrgUnit(@PathVariable Long id) throws UserOfficeNotFoundException, Exception {
        OrgUnit orgUnit = orgUnitService.findById(id);
        if (orgUnit == null) {
            throw new UserOfficeNotFoundException(String.format(orgUnitNotFound, id.toString()));
        }

        OrgUnitsData response = fillShortResponse(orgUnit.getChildren());
        return ResponseEntity.ok().body(response);
    }


    //@GetMapping (path = "/orgunits", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<OrgUnitsDataFull> listFullOrgUnits() throws UserOfficeNotFoundException, Exception {
        OrgUnitsDataFull response = fillFullResponse(orgUnitService.findAll());
        return ResponseEntity.ok().body(response);
    }

    @Override
    //@GetMapping (path = "/orgunits/type/{orgUnitType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrgUnitsData> listOrgUnitByType(@PathVariable Long orgUnitType) throws OrgUnitTypeNotFoundException,Exception {
        OrgUnitsData response = fillShortResponse(orgUnitService.findOUByType(orgUnitType.intValue()));
        return ResponseEntity.ok().body(response);
    }

    private OrgUnitsData fillShortResponse(List<OrgUnit> orgUnitList) {
        OrgUnitsData response = new OrgUnitsData();
        response.setOrgUnits(new ArrayList<>());
        if (CollectionUtils.isNotEmpty(orgUnitList)) {
            response.getOrgUnits().addAll(
                    orgUnitList.parallelStream().map(
                            OrgUnitController::mapOrgUnit2ShortData
                    ).collect(Collectors.toList()));
        }
        return response;
    }

    private OrgUnitsDataFull fillFullResponse(List<OrgUnit> orgUnitList) {
        OrgUnitsDataFull response = new OrgUnitsDataFull();
        response.setOrgUnits(new ArrayList<>());

        if (CollectionUtils.isNotEmpty(orgUnitList)) {
            List<OrgUnitDataFull> branches = fillBranches(orgUnitList);
            response.getOrgUnits().addAll(branches);
        }
        return response;
    }
    private List<OrgUnitDataFull> fillBranches(List<OrgUnit> orgUnitList){
        List<OrgUnitDataFull> orgUnitFullDataList = new ArrayList<>();

        List<OrgUnit> branches = filterByType(orgUnitList, OrgUnitType.BRANCH);

        orgUnitFullDataList.addAll(branches.parallelStream()
                .map(OrgUnitController::mapOrgUnit2Branch)
                .collect(Collectors.toList()));

        return orgUnitFullDataList;
    }

    private List<OrgUnit> filterByType(List<OrgUnit> orgUnitList, OrgUnitType type) {
        return orgUnitList.parallelStream()
                .filter(f-> type.ordinal() == f.getType().ordinal() )
                .collect(Collectors.toList());
    }

    private static OrgUnitData mapOrgUnit2ShortData(OrgUnit orgUnit) {
        OrgUnitData orgUnitShortData = new OrgUnitData();
        orgUnitShortData.setId(orgUnit.getId());
        orgUnitShortData.setOffice(orgUnit.getName());
        if (orgUnit.getParent() != null) {
            orgUnitShortData.setBranch(orgUnit.getParent().getName());
        }
        return orgUnitShortData;
    }

    private static OrgUnitDataFull mapOrgUnit2Branch(OrgUnit orgUnit) {
        OrgUnitDataFull orgUnitFullData = new OrgUnitDataFull();
        orgUnitFullData.setBranchId(orgUnit.getId());
        orgUnitFullData.setName(orgUnit.getName());
        List<OfficeData> offices = new ArrayList<>();
        if (orgUnit.getChildren() != null && orgUnit.getChildren().size() > 0) {
            offices.addAll(
                    orgUnit.getChildren().parallelStream()
                            .map(OrgUnitController::mapOrgUnit2Office)
                            .collect(Collectors.toList())
            );
        }
        orgUnitFullData.setOffices(offices);
        return orgUnitFullData;
    }

    private static OfficeData mapOrgUnit2Office(OrgUnit orgUnit) {
        OfficeData officeData = new OfficeData();
        officeData.setOfficeId(orgUnit.getId());
        officeData.setName(orgUnit.getName());
        officeData.setCity(orgUnit.getCity());
        officeData.setAddress(orgUnit.getAddress());
        return officeData;
    }


    @ExceptionHandler
    ResponseEntity handleException(final OrgUnitTypeNotFoundException exception) {
        LOGGER.error("Ошибка обработки запроса: некорректный тип орг.структуры", exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorRes(ORGUNIT_TYPE_FIELD_NAME, exception.getMessage()));
    }

}