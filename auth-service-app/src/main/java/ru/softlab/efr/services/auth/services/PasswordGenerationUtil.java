package ru.softlab.efr.services.auth.services;

import org.apache.log4j.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

/**
 * Утилита создания хешированых паролей для мобильного сервиса.
 *
 * @author danilov
 * @since 26.02.2019
 */
class PasswordGenerationUtil {
    private static final Logger LOGGER = Logger.getLogger(AuthenticateService.class);

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    private PasswordGenerationUtil() {
    }

    static String getSalt() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.warn("Error while hashing a password: " + e.getMessage(), e);
            throw new AssertionError();
        } finally {
            spec.clearPassword();
        }
    }

    static String generateSecurePassword(String password, String salt) {
        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());
        return Base64.getEncoder().encodeToString(securePassword);
    }
}
