package ru.softlab.efr.services.auth.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Хелпер для работы с LocalData
 *
 * @author khudyakov
 * @since 21.10.2018.
 */
public class DateHelper {

    /**
     * LocalDate to Date
     * @param localDate LocalDate
     * @return Date
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Date to LocalDate
     * @param date дата
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
