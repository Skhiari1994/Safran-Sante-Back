package com.arabsoft.Gestion_adherent.Configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText(); // Get the input text
        if (text == null || text.trim().isEmpty()) {
            return null; // Return null for empty or null input
        }
        try {
            return LocalDate.parse(text, FORMATTER); // Attempt to parse the date
        } catch (DateTimeParseException e) {
            throw new IOException("Invalid date format, expected dd/MM/yyyy: " + text, e);
        }
    }
}

