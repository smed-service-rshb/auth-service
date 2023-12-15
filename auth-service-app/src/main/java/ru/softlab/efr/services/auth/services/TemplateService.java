package ru.softlab.efr.services.auth.services;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.softlab.efr.common.client.PrintTemplatesClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


@Service
public class TemplateService {

    private static final Logger LOGGER = Logger.getLogger(TemplateService.class);
    private static final Long COMMON_DICT_TIMEOUT = 10L;

    private PrintTemplatesClient printTemplatesClient;
    private MotivationDocumentSettingsService motivationDocumentSettingsService;

    @Autowired
    public TemplateService(PrintTemplatesClient printTemplatesClient,
                           MotivationDocumentSettingsService motivationDocumentSettingsService) {
        this.printTemplatesClient = printTemplatesClient;
        this.motivationDocumentSettingsService = motivationDocumentSettingsService;
    }

    public Resource getTemplateContent(String templateId) throws Exception {
        return printTemplatesClient.getContent(templateId, COMMON_DICT_TIMEOUT);
    }

    public byte[] getMotivationPrintForm() {
        Resource template = null;
        try {
            template = getTemplateContent(motivationDocumentSettingsService.getIdSettings());
        } catch (Exception e) {
            LOGGER.error(String.format("Шаблон не найден: %s", e));
        }
        if (Objects.nonNull(template)) {
            try {
                return IOUtils.toByteArray(template.getInputStream());
            } catch (IOException e) {
                LOGGER.error(String.format("Объект элемента шаблонов пуст: %s", e));
            }
        }
        return null;
    }

    public String getMotivationFileName() throws UnsupportedEncodingException {
        return URLEncoder.encode(String.format("%s%s", motivationDocumentSettingsService.getFilenameSettings(), ".docx"),
                StandardCharsets.UTF_8.name()).replaceAll("\\+", " ");
    }

}
