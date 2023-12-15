package ru.softlab.efr.test.services.auth.rest;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;
import ru.softlab.efr.services.authorization.PrincipalDataSerializer;
import ru.softlab.efr.services.authorization.config.PermissionControlConfig;
import ru.softlab.efr.test.infrastructure.transport.rest.MockRest;
import ru.softlab.efr.test.infrastructure.transport.rest.MockRestRule;

import java.io.IOException;
import java.util.Arrays;


/**
 * Утилита для интеграционного тестирования REST-сервисов с данными авторизации.
 * <p>
 * Пример использования:
 * <pre>
 *      &#064;Rule
 *      public final AuthorizedRestRule mockRest = new AuthorizedRestRule(original);
 *
 *      ...
 *
 *      mockRest
 *          .init()
 *          .path("test/clients/{clientId}")
 *          .variable("clientId", 1234)
 *          .get(Client.class)
 *          .andExpect(status().isOk())
 *          .andExpect(content().hasId(1234))
 *          .andExpect(content().body(hasProperty("name", is("Василий")));
 * </pre>
 *
 * @author akrenev
 * @see MockRestRule
 * @since 27.02.2018
 */
public class AuthorizedRestRule implements TestRule {
    public static final String FULL_ACCESS_ROLE = "it-full-access-role";

    private static final Long MOCK_PRINCIPAL_ID = 1L;
    private static final String MOCK_PRINCIPAL_FIRST_NAME = "Имя";
    private static final String MOCK_PRINCIPAL_MIDDLE_NAME = "Отчество";
    private static final String MOCK_PRINCIPAL_SECOND_NAME = "Фамилия";
    private static final String MOCK_PRINCIPAL_OFFICE = "Офис";
    private static final String MOCK_PRINCIPAL_BRANCH = "Филиал";
    private static final String MOCK_PRINCIPAL_PERSONNEL_NUMBER = "Табельный номер";

    private static final PrincipalDataSerializer serializer = new PrincipalDataSerializer();

    private MockRestRule original;

    /**
     * Конструктор
     *
     * @param original правило заглушки транспортной компоненты
     */
    public AuthorizedRestRule(MockRestRule original) {
        this.original = original;
    }

    /**
     * Инициализация тестового запроса с данными авторизации (клиент с ролью полных прав)
     *
     * @return запрос
     */
    public MockRest init() {
        return init(Right.values());
    }

    /**
     * Инициализация тестового запроса с данными авторизации (клиент с переданными правами)
     *
     * @param rights права клиента
     * @return запрос
     */
    public MockRest init(Right... rights) {
        PrincipalDataImpl principalData = new PrincipalDataImpl();
        principalData.setId(MOCK_PRINCIPAL_ID);
        principalData.setFirstName(MOCK_PRINCIPAL_FIRST_NAME);
        principalData.setMiddleName(MOCK_PRINCIPAL_MIDDLE_NAME);
        principalData.setSecondName(MOCK_PRINCIPAL_SECOND_NAME);
        principalData.setOffice(MOCK_PRINCIPAL_OFFICE);
        principalData.setBranch(MOCK_PRINCIPAL_BRANCH);
        principalData.setPersonnelNumber(MOCK_PRINCIPAL_PERSONNEL_NUMBER);
        principalData.setRights(Arrays.asList(rights));
        return init(principalData);
    }

    /**
     * Инициализация тестового запроса с данными авторизации (переданный клиент)
     *
     * @param principalData клиент
     * @return запрос
     */
    public MockRest init(PrincipalData principalData) {
        try {
            MockRest mockRest = original.init();
            mockRest.header(PermissionControlConfig.HTTP_HEAD, serializer.serialize(principalData));
            return mockRest;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка формирования тела заголовка авторизации.", e);
        }
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return original.apply(base, description);
    }
}