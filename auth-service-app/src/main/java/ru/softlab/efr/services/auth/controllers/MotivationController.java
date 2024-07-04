package ru.softlab.efr.services.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.converter.MotivationConverter;
import ru.softlab.efr.services.auth.exchange.model.*;
import ru.softlab.efr.services.auth.model.MotivationEntity;
import ru.softlab.efr.services.auth.services.MotivationService;
import ru.softlab.efr.services.auth.services.MotivationSettingsService;
import ru.softlab.efr.services.authorization.annotations.HasRight;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@RestController
public class MotivationController implements MotivationApi {

    private MotivationService motivationService;
    private MotivationConverter motivationConverter;
    private MotivationSettingsService motivationSettingsService;

    @Autowired
    public MotivationController(MotivationService motivationService,
                                MotivationConverter motivationConverter,
                                MotivationSettingsService motivationSettingsService) {
        this.motivationService = motivationService;
        this.motivationConverter = motivationConverter;
        this.motivationSettingsService = motivationSettingsService;
    }

    @Override
    @HasRight(Right.CONTROL_MOTIVATION_PROGRAMS)
    public ResponseEntity<MotivationProgramAdmin> getUserMotivationLatest(@PathVariable("userId") Long userId) throws Exception {
        if (motivationSettingsService.getCurrentSettings().isIsEnabled()) {
            MotivationProgramAdmin motivationProgramAdmin = motivationConverter.convertToAdminMotivation(
                    Optional.ofNullable(motivationService.getMotivationByUserId(userId)).orElse(new MotivationEntity())
            );
            if (Objects.nonNull(motivationProgramAdmin.getId())) {
                motivationProgramAdmin.setDocumentName(
                        motivationService.getFilenameByMotivationId(motivationProgramAdmin.getId(), MotivationDocumentTypes.CHECK_FORM));
                motivationProgramAdmin.setPassportName(
                        motivationService.getFilenameByMotivationId(motivationProgramAdmin.getId(), MotivationDocumentTypes.PASSPORT));
            }
            return ResponseEntity.ok(motivationProgramAdmin);
        } else {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
    }

    @Override
    @HasRight(Right.CONTROL_MOTIVATION_PROGRAMS)
    public ResponseEntity<MotivationProgramAdminList> getUserMotivationsList(@PathVariable("userId") Long userId) throws Exception {
        if (motivationSettingsService.getCurrentSettings().isIsEnabled()) {
            MotivationProgramAdminList motivationProgramAdminList = new MotivationProgramAdminList();
            List<MotivationProgramAdmin> motivationPrograms = motivationService.getMotivationsListByUserId(userId)
                    .stream()
                    .map(motivationConverter::convertToAdminMotivation)
                    .collect(Collectors.toList());
            motivationPrograms.forEach(motivationProgramAdmin -> {
                        motivationProgramAdmin.setDocumentName(
                                motivationService.getFilenameByMotivationId(motivationProgramAdmin.getId(), MotivationDocumentTypes.CHECK_FORM));
                        motivationProgramAdmin.setPassportName(
                                motivationService.getFilenameByMotivationId(motivationProgramAdmin.getId(), MotivationDocumentTypes.CHECK_FORM));
                    }
            );
            motivationProgramAdminList.setElements(motivationPrograms);
            return ResponseEntity.ok(motivationProgramAdminList);
        } else {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
    }

    @Override
    @HasRight(Right.MOTIVATION_REPORT_CREATE)
    public ResponseEntity<byte[]> getXlsxReport(@Valid @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate startDate,
                                                @Valid @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate endDate) throws Exception {
        if (motivationSettingsService.getCurrentSettings().isIsEnabled()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + motivationService.getAdminReportFilename())
                    .body(motivationService.getAdminReport(startDate, endDate));
        } else {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
    }

    @Override
    @HasRight(Right.CONTROL_MOTIVATION_PROGRAMS)
    public ResponseEntity motivationByMotivationIdEdit(@PathVariable("motivationId") Long motivationId,
                                                       @Valid @RequestBody MotivationSettingsEditAdmin updateMotivationSettingsRq) throws Exception {
        if (motivationSettingsService.getCurrentSettings().isIsEnabled()) {
            ErrorModel responseErrors = motivationService.modifyMotivationAdmin(motivationId,
                    updateMotivationSettingsRq.getAccountNumber(),
                    updateMotivationSettingsRq.getBankName(), updateMotivationSettingsRq.getBikCode(),
                    updateMotivationSettingsRq.getInn(),
                    updateMotivationSettingsRq.getRegistrationAddress(), updateMotivationSettingsRq.getIndex(),
                    updateMotivationSettingsRq.getComment(), updateMotivationSettingsRq.getMotivationCorrectStatus(),
                    updateMotivationSettingsRq.getStartDate());
            if (responseErrors.getErrors().isEmpty()) {
                return ResponseEntity.ok().build();
            } else {
                if (responseErrors.getErrors().get(0).getKey().equals("Проверка допустимости вобранной даты начала мотивации")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseErrors);
                }
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(responseErrors);
            }
        } else {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
    }

    @Override
    @HasRight(Right.VIEW_MOTIVATION_PROGRAMS)
    public ResponseEntity<MotivationProgramUser> getMotivation() throws Exception {
        if (motivationSettingsService.getCurrentSettings().isIsEnabled()) {
            MotivationProgramUser motivationProgramUser = motivationConverter.convertToUserMotivation(
                    Optional.ofNullable(motivationService.getMotivation()).orElse(new MotivationEntity())
            );
            if (Objects.nonNull(motivationProgramUser.getId())) {
                motivationProgramUser.setDocumentName(motivationService.getFilenameForUser(MotivationDocumentTypes.CHECK_FORM));
                motivationProgramUser.setPassportName(motivationService.getFilenameForUser(MotivationDocumentTypes.PASSPORT));
            }
            return ResponseEntity.ok(motivationProgramUser);
        } else {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
    }

    @Override
    @HasRight(Right.TAKE_PART_IN_MOTIVATION_PROGRAMS)
    public ResponseEntity postMotivation(@Valid @RequestBody MotivationProgramUserModify motivationProgramUserModify) throws Exception {
        if (motivationSettingsService.getCurrentSettings().isIsEnabled()) {
            Object response = motivationService.addOrModifyMotivation(
                    motivationProgramUserModify.getAccountNumber(),
                    motivationProgramUserModify.getBikCode(),
                    motivationProgramUserModify.getBankName(),
                    motivationProgramUserModify.getInn(),
                    motivationProgramUserModify.getRegistrationAddress(),
                    motivationProgramUserModify.getIndex(),
                    motivationProgramUserModify.getStartDate()
            );
            if (Objects.isNull(response)) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
            }
            if (response instanceof MotivationEntity) {
                return ResponseEntity.ok(motivationConverter.convertToUserMotivation((MotivationEntity) response));
            } else {
                ErrorModel errorModel = new ErrorModel();
                if (response instanceof ErrorModel) {
                    errorModel = (ErrorModel) response;
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorModel);
            }
        } else {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
    }

    @Override
    @HasRight(Right.VIEW_MOTIVATION_PROGRAMS)
    public ResponseEntity<byte[]> printMotivation() throws Exception {
        if (motivationSettingsService.getCurrentSettings().isIsEnabled()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=")
                    .body(new byte[0]);
        } else {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
    }

    @Override
    @HasRight(Right.TAKE_PART_IN_MOTIVATION_PROGRAMS)
    public ResponseEntity<String> uploadMotivationAttachment(@PathVariable("motivationDocumentTypes") String motivationDocumentTypes,
                                                             @Valid @RequestPart(value = "content", required = false) MultipartFile content) throws Exception {
        if (motivationSettingsService.getCurrentSettings().isIsEnabled()) {
            if (motivationService.attachDocument(content, MotivationDocumentTypes.fromValue(motivationDocumentTypes))) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
    }

    @Override
    @HasRight(Right.VIEW_MOTIVATION_PROGRAMS)
    public ResponseEntity<byte[]> getMotivationAttachment(@PathVariable("motivationDocumentTypes") String motivationDocumentTypes) throws Exception {
        if (motivationSettingsService.getCurrentSettings().isIsEnabled()) {
            String filename = URLEncoder.encode(String.format("%s", motivationService.getFilenameForUser(MotivationDocumentTypes.fromValue(motivationDocumentTypes))),
                    StandardCharsets.UTF_8.name()).replaceAll("\\+", " ");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .body(motivationService.getMotivationFileForUser(MotivationDocumentTypes.fromValue(motivationDocumentTypes)));
        } else {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
    }

    @Override
    @HasRight(Right.CONTROL_MOTIVATION_PROGRAMS)
    public ResponseEntity<byte[]> getMotivationAttachmentById(@PathVariable("motivationId") Long motivationId,
                                                              @PathVariable("motivationDocumentTypes") String motivationDocumentTypes) throws Exception {
        if (motivationSettingsService.getCurrentSettings().isIsEnabled()) {
            String filename = URLEncoder.encode(String.format("%s", motivationService.getFilenameByMotivationId(motivationId, MotivationDocumentTypes.fromValue(motivationDocumentTypes))),
                    StandardCharsets.UTF_8.name()).replaceAll("\\+", " ");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .body(motivationService.getMotivationFileByMotivation(motivationId, MotivationDocumentTypes.fromValue(motivationDocumentTypes)));
        } else {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
    }

    @Override
    @HasRight(Right.TAKE_PART_IN_MOTIVATION_PROGRAMS)
    public ResponseEntity<Boolean> getMotivationWindowHide() throws Exception {
        if (motivationSettingsService.getCurrentSettings().isIsEnabled()) {
            return ResponseEntity.ok(motivationService.isCurrentUserMotivationFilled());
        } else {
            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }
    }

}
