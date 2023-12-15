package ru.softlab.efr.services.test.auth;

import ru.softlab.efr.services.auth.Right;
import ru.softlab.efr.services.auth.exchange.model.PasswordCharset;
import ru.softlab.efr.services.auth.exchange.model.PasswordCheckSettings;
import ru.softlab.efr.services.authorization.PrincipalData;
import ru.softlab.efr.services.authorization.PrincipalDataImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockData {
    public static PrincipalData getPrincipalData(){
        PrincipalDataImpl principalData = new PrincipalDataImpl();
        principalData.setId(1L);
        principalData.setFirstName("Евгений");
        principalData.setSecondName("Осминин");
        principalData.setMiddleName("Сергеевич");
        principalData.setRights(Arrays.asList(Right.values()));
        return principalData;
    }

    public static PasswordCheckSettings getPasswordCheckSettings(){
        PasswordCheckSettings settings = new PasswordCheckSettings();

        List<PasswordCharset> listEnabled = new ArrayList<>();
        List<PasswordCharset> listRequired = new ArrayList<>();

        listEnabled.add(PasswordCharset.LOWERCASE_LATIN);
        listEnabled.add(PasswordCharset.SPECIAL);

        listRequired.add(PasswordCharset.UPPERCASE_LATIN);
        listRequired.add(PasswordCharset.DIGIT);

        settings.setCheckEnabled(false);
        settings.setMinLength(1);
        settings.setMaxLength(60);
        settings.setNumberOfDifferentCharacters(8);
        settings.setSpecialCharsets("!@#$%^&*()-_+=");
        settings.setEnabledCharsets(listEnabled);
        settings.setRequiredCharsets(listRequired);

        return settings;
    }
}
