package ru.softlab.efr.services.auth.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Random;

@PropertySource("classpath:password.properties")
@Service
public class PasswordService {

    @Value("${alphabet}")
    private String alphabet;

    @Value("${password-length}")
    private Integer length;


    public String generatePassword() {
        return generatePassword(length, alphabet);
    }

    public String generatePassword(int length, String alphabet) {
        StringBuilder password;
        Random random = new Random();
        do {
            password = new StringBuilder(length);
            for (int i = 0; i < length; ++i) {
                password.append(alphabet.charAt(random.nextInt(alphabet.length())));
            }
        } while (password.length() < length);
        return password.toString();
    }

    /**
     * Получить hash от пароля
     * @param password пароль
     * @return хэш от пароля
     */
    public String getPasswordHash(String password) {
        return DigestUtils.sha1Hex(password);
    }
}
