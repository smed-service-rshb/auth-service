package ru.softlab.efr.services.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.exchange.model.MotivationSettings;
import ru.softlab.efr.services.auth.services.MotivationSettingsService;
import ru.softlab.efr.services.authorization.annotations.HasRight;

import javax.validation.Valid;

@Validated
@RestController
public class MotivationCheckSettingsController implements MotivationCheckSettingsApi {

    private MotivationSettingsService motivationSettingsService;

    @Autowired
    public MotivationCheckSettingsController(MotivationSettingsService motivationSettingsService) {
        this.motivationSettingsService = motivationSettingsService;
    }


    @Override
    public ResponseEntity<MotivationSettings> getMotivationCheckSettings() throws Exception {
        return ResponseEntity.ok(motivationSettingsService.getCurrentSettings());
    }

    @Override
    @HasRight(Right.CONTROL_MOTIVATION_PROGRAMS)
    public ResponseEntity<Void> updateMotivationCheckSettings(@Valid @RequestBody MotivationSettings updateMotivationSettingsRq) throws Exception {
        motivationSettingsService.update(updateMotivationSettingsRq);
        return ResponseEntity.ok().build();
    }
}
