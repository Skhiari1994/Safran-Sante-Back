package com.tn.arabsoft.remboursement_frais_medicaux.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateParser {

    // Empêche l'instanciation
    private DateParser() {
        throw new IllegalStateException("Utility class");
    }

    private static final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ISO_LOCAL_DATE,
            // Common European formats
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            DateTimeFormatter.ofPattern("d.M.yyyy"),

            // Compact formats
            DateTimeFormatter.ofPattern("yyyyMMdd"),
            DateTimeFormatter.ofPattern("ddMMyyyy"),

            // With month names (English)
            DateTimeFormatter.ofPattern("dd MMM yyyy"), // 06 May 2026
            DateTimeFormatter.ofPattern("d MMM yyyy"),
            DateTimeFormatter.ofPattern("dd MMMM yyyy"), // 06 May 2026 (full month)
            DateTimeFormatter.ofPattern("d MMMM yyyy"),

            // US format (⚠️ risky)
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("M/d/yyyy")

    );

    public static LocalDate parse(String dateStr) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException ignored) {
                // ne rien faire continuer avec le prochain format
            }
        }
        throw new IllegalArgumentException("Invalid date format: " + dateStr);
    }

}
