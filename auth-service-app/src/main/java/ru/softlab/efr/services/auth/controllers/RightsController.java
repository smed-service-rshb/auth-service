package ru.softlab.efr.services.auth.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.exchange.model.GetAllRightsRs;
import ru.softlab.efr.services.auth.exchange.model.RightData;
import ru.softlab.efr.services.auth.model.Right;
import ru.softlab.efr.services.auth.services.RightStoreService;
import ru.softlab.efr.services.authorization.annotations.HasPermission;

import java.util.ArrayList;
import java.util.List;

import static ru.softlab.efr.services.auth.config.Permissions.GET_RIGHTS;

/**
 * Контроллер обработки запросов, связанных с правами
 */
@Validated
@RestController
public class RightsController implements RightsApi {
    private static final Logger LOGGER = Logger.getLogger(RightsController.class);

    @Autowired
    @Qualifier("rightsServiceDaoImpl")
    private RightStoreService rightStoreService;


    @HasPermission (GET_RIGHTS)
    @Override
    public ResponseEntity<GetAllRightsRs> getAllRights() throws Exception {
        List<Right> rights = rightStoreService.getAll();
        List<RightData> responseData = buildRightsData(rights);
        return ResponseEntity.ok().body(new GetAllRightsRs(responseData));
    }

    private List<RightData> buildRightsData(List<Right> rights) {
        List<RightData> responseData = new ArrayList<>(rights.size());
        for (Right right : rights) {
            RightData rightData = new RightData();
            rightData.setExternalId(right.getExternalId().toString());
            rightData.setDescription(right.getDescription());
            responseData.add(rightData);
        }
        return responseData;
    }
}
