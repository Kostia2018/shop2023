package ru.javawebinar.topjava.util;

import jdk.jshell.execution.LoaderDelegate;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final LocalDateTime MIN = LocalDateTime.of(1, 1, 1, 0, 0);

    private static final LocalDateTime MAX = LocalDateTime.of(3000, 1, 1, 0, 0);

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }


    public static LocalDateTime getStart(LocalDate starDate) {

        return starDate != null ? starDate.atStartOfDay() : MIN;

    }

    public static LocalDateTime getEnd(LocalDate endDate) {

        return endDate != null ? endDate.plus(1, ChronoUnit.DAYS).atStartOfDay() : MAX;

    }

    public LocalDate parsDate(String date) {

        return StringUtils.isEmpty(date) ? null : LocalDate.parse(date);


    }

    public LocalTime parsTime(String time) {
        return StringUtils.isEmpty(time) ? null : LocalTime.parse(time);


    }

}

