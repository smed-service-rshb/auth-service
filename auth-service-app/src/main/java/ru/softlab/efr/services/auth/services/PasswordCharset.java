package ru.softlab.efr.services.auth.services;

import java.util.regex.Pattern;

/**
 * Множество групп символов, использующихся для настройки требований к паролям пользователей.
 *
 * @author andrey Grigorov
 */
public enum PasswordCharset {

    /**
     * Цифры.
     */
    DIGIT("Цифры", Pattern.compile("[0-9]")),

    /**
     * Латинские прописные буквы.
     */
    LOWERCASE_LATIN("Латинские прописные буквы", Pattern.compile("[a-z]")),

    /**
     * Латинские заглавные буквы.
     */
    UPPERCASE_LATIN("Латинские заглавные буквы", Pattern.compile("[A-Z]")),

    /**
     * Специальные символы.
     */
    SPECIAL("Специальные символы", null);

    private String title;
    private Pattern pattern;

    PasswordCharset(String title, Pattern pattern) {
        this.title = title;
        this.pattern = pattern;
    }

    public String getTitle() {
        return title;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
