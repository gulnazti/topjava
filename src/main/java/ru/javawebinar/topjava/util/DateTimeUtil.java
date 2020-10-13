package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.util.StringUtils.isEmpty;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime == null ? LocalTime.MIN : startTime) >= 0
               && lt.compareTo(endTime == null ? LocalTime.MAX : endTime) < 0;
    }

    public static boolean isBetween(LocalDate ld, LocalDate startDate, LocalDate endDate) {
        return ld.compareTo(startDate == null ? LocalDate.MIN : startDate) >= 0
               && ld.compareTo(endDate == null ? LocalDate.MAX : endDate) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseDate(String date) {
        return isEmpty(date) ? null : LocalDate.parse(date);
    }

    public static LocalTime parseTime(String time) {
        return isEmpty(time) ? null : LocalTime.parse(time);
    }
}

