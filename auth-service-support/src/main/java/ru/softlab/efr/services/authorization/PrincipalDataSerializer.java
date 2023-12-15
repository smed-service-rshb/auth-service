package ru.softlab.efr.services.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Сериализатор/десериализатор данных авторизации
 *
 * @author akrenev
 * @since 27.02.2018
 */
public class PrincipalDataSerializer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Сериализовать данные авторизации
     *
     * @param principal данные авторизации
     * @return сериализованные данные авторизации
     * @throws IOException ошибка сериализации
     */
    public String serialize(PrincipalData principal) throws IOException {
        String jsonInString = OBJECT_MAPPER.writeValueAsString(principal);
        return Base64.getEncoder().encodeToString(jsonInString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Десериализовать данные авторизации
     *
     * @param principal сериализованные данные авторизации
     * @return данные авторизации
     * @throws IOException ошибка десериализации
     */
    public PrincipalData deserialize(String principal) throws IOException {
        String jsonPrincipalData = new String(Base64.getDecoder().decode(principal), StandardCharsets.UTF_8);
        return OBJECT_MAPPER.readValue(jsonPrincipalData, PrincipalDataImpl.class);
    }
}
