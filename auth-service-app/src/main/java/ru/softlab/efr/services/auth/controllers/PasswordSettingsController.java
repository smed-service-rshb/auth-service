package ru.softlab.efr.services.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.config.Permissions;
import ru.softlab.efr.services.auth.exchange.model.PasswordCheckSettings;
import ru.softlab.efr.services.auth.services.PasswordSettingsService;
import ru.softlab.efr.services.authorization.annotations.HasPermission;

import javax.validation.Valid;

@Validated
@RestController
public class PasswordSettingsController implements PasswordCheckSettingsApi {

    @Autowired
    PasswordSettingsService passwordSettingsService;

    @Override
    @HasPermission(Permissions.GET_EMPLOYEES)
    public ResponseEntity<PasswordCheckSettings> getPasswordCheckSettings() {
        return ResponseEntity.ok(passwordSettingsService.getCurrentSettings());
    }

    @Override
    @HasPermission(Permissions.UPDATE_EMPLOYEE)
    public ResponseEntity<Void> updatePasswordCheckSettings(@Valid @RequestBody PasswordCheckSettings updatePasswordCheckSettingsRq) {
        passwordSettingsService.update(updatePasswordCheckSettingsRq);
        return ResponseEntity.ok().build();
    }
}
