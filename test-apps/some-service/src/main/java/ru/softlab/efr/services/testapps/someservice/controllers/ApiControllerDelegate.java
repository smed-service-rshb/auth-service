package ru.softlab.efr.services.testapps.someservice.controllers;

import org.springframework.stereotype.Component;
import ru.softlab.efr.services.authorization.annotations.HasPermission;
import ru.softlab.efr.services.testapps.someservice.config.Permissions;

/**
 * Делегат ApiController
 */
@Component
public class ApiControllerDelegate implements ApiController.Delegate {

    @Override
    @HasPermission(Permissions.PERMISSION1)
    public String doHandleRequest(String message) {
        return message;
    }
}
