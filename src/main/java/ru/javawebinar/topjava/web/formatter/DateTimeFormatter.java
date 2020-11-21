package ru.javawebinar.topjava.web.formatter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

/**
 * @author gulnaz
 */
public class DateTimeFormatter {
    public static class DateFormatter implements Formatter<LocalDate> {
        @Override
        public LocalDate parse(String string, Locale locale) {
            return DateTimeUtil.parseLocalDate(string);
        }

        @Override
        public String print(LocalDate localDate, Locale locale) {
            return localDate.format(ISO_LOCAL_DATE);
        }
    }

    public static class TimeFormatter implements Formatter<LocalTime> {
        @Override
        public LocalTime parse(String string, Locale locale) {
            return DateTimeUtil.parseLocalTime(string);
        }

        @Override
        public String print(LocalTime localTime, Locale locale) {
            return localTime.format(ISO_LOCAL_TIME);
        }
    }
}
