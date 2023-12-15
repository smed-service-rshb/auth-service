package ru.softlab.efr.services.auth.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.softlab.efr.services.auth.exchange.model.MotivationDocumentTypes;
import ru.softlab.efr.services.auth.model.Employee;
import ru.softlab.efr.services.auth.model.MotivationAttachmentsEntity;
import ru.softlab.efr.services.auth.model.MotivationEntity;
import ru.softlab.efr.services.auth.services.impl.dao.repositories.MotivationAttachmentsRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

@Service
public class MotivationAttachmentService {

    private static final Logger LOGGER = Logger.getLogger(MotivationAttachmentService.class);

    private MotivationAttachmentsRepository motivationAttachmentsRepository;

    @Autowired
    public MotivationAttachmentService(MotivationAttachmentsRepository motivationAttachmentsRepository) {
        this.motivationAttachmentsRepository = motivationAttachmentsRepository;
    }

    @Transactional(readOnly = true)
    public String getFilenameByUserAndMotivation(Employee user, MotivationEntity motivation, MotivationDocumentTypes motivationDocumentTypes) {
        MotivationAttachmentsEntity attachment = motivationAttachmentsRepository.getByUserAndMotivationEntityAndIsDeletedAndMotivationDocumentTypes(
                user,
                motivation,
                false,
                motivationDocumentTypes);
        if (Objects.nonNull(attachment)) {
            return attachment.getFileName();
        }
        LOGGER.info("Файл не найден");
        return null;
    }

    @Transactional(readOnly = true)
    public String getFilenameByMotivation(MotivationEntity motivation, MotivationDocumentTypes motivationDocumentTypes) {
        MotivationAttachmentsEntity attachment = motivationAttachmentsRepository.getByMotivationEntityAndIsDeletedAndMotivationDocumentTypes(
                motivation,
                false,
                motivationDocumentTypes);
        if (Objects.nonNull(attachment)) {
            return attachment.getFileName();
        }
        LOGGER.info("Файл не найден");
        return null;
    }

    @Transactional
    public boolean attachOrModify(Employee user, MotivationEntity motivation, MultipartFile content, MotivationDocumentTypes motivationDocumentTypes) {
        MotivationAttachmentsEntity attachment = Optional.ofNullable(
                motivationAttachmentsRepository.getByUserAndMotivationEntityAndIsDeletedAndMotivationDocumentTypes(user, motivation, false, motivationDocumentTypes))
                .orElse(new MotivationAttachmentsEntity());

        if (!content.isEmpty()) {
            attachment.setUser(user);
            attachment.setMotivationEntity(motivation);
            attachment.setFileName(content.getOriginalFilename());
            attachment.setDeleted(false);
            attachment.setMotivationDocumentTypes(motivationDocumentTypes);
            attachment.setCreationDate();
            attachment =  motivationAttachmentsRepository.save(attachment);

            try {
                motivationAttachmentsRepository.setContent(attachment.getId(), content.getBytes());
                return true;
            } catch (IOException e) {
                LOGGER.error(e);
                return false;
            }
        }
        LOGGER.info("Нет контента для записи");
        return false;
    }

    @Transactional(readOnly = true)
    public byte[] getFileByMotivationAndUser(Employee user, MotivationEntity motivation, MotivationDocumentTypes motivationDocumentTypes) {
        MotivationAttachmentsEntity attachment = motivationAttachmentsRepository.getByUserAndMotivationEntityAndIsDeletedAndMotivationDocumentTypes(
                user,
                motivation,
                false,
                motivationDocumentTypes);
        if (Objects.nonNull(attachment)) {
            return motivationAttachmentsRepository.getContent(attachment.getId());
        }
        LOGGER.info("Файл не найден");
        return null;
    }

    @Transactional(readOnly = true)
    public byte[] getFileByMotivation(MotivationEntity motivation, MotivationDocumentTypes motivationDocumentTypes) {
        MotivationAttachmentsEntity attachment = motivationAttachmentsRepository.getByMotivationEntityAndIsDeletedAndMotivationDocumentTypes(
                motivation,
                false,
                motivationDocumentTypes);
        if (Objects.nonNull(attachment)) {
            return motivationAttachmentsRepository.getContent(attachment.getId());
        }
        LOGGER.info("Файл не найден");
        return null;
    }
}
