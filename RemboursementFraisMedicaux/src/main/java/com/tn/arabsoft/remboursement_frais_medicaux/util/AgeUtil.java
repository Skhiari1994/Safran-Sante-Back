package com.tn.arabsoft.remboursement_frais_medicaux.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

public class AgeUtil {

    // Order matters: the first format that parses wins
    private static final List<DateTimeFormatter> FORMATS = List.of(
            fmt("uuuu-MM-dd"),
            fmt("d/M/uuuu"),
            fmt("M/d/uuuu"),
            fmt("d-M-uuuu"),
            fmt("uuuu/M/d"),
            fmt("d.M.uuuu"));

    private static DateTimeFormatter fmt(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.STRICT);
    }

    public static LocalDate parseDate(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Date is empty");
        }
        String s = input.trim();
        // keep only the date part if a time is attached (2025-05-01T10:30:00 or
        // "2025-05-01 10:30")
        s = s.split("[T ]")[0];

        for (DateTimeFormatter f : FORMATS) {
            try {
                return LocalDate.parse(s, f);
            } catch (DateTimeParseException ignored) {
                // try the next format
            }
        }
        throw new IllegalArgumentException("Unsupported date format: " + input);
    }

    public record Age(int years, int months) {
    }

    public static Age calculateAge(String birthDateStr) {
        return calculateAge(parseDate(birthDateStr), LocalDate.now());
    }

    public static Age calculateAge(LocalDate birthDate, LocalDate refDate) {
        if (birthDate.isAfter(refDate)) {
            throw new IllegalArgumentException("Birth date is in the future");
        }
        var p = java.time.Period.between(birthDate, refDate);
        return new Age(p.getYears(), p.getMonths());
    }

}
