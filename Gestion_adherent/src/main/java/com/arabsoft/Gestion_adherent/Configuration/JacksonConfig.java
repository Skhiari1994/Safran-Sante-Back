package com.arabsoft.gestion_adherent.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Les écrans envoient parfois des champs que le serveur ne stocke pas (ex. mat_int) : on les ignore
        // au lieu de refuser toute la requête (ce qui faisait échouer l'ajout d'un adhérent).
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Register JSR310 module to handle Java 8 date/time classes
        mapper.registerModule(new JavaTimeModule());

        // Register custom serializer and deserializer for LocalDate
        SimpleModule module = new SimpleModule();
        // Une liste non choisie arrive du front sous la forme "" : sous Oracle "" valait NULL, mais SQL Server
        // la stocke telle quelle et les contraintes (CHECK, clés étrangères) la refusent. On revient donc à NULL.
        module.addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String valeur = StringDeserializer.instance.deserialize(p, ctxt);
                return valeur != null && valeur.isEmpty() ? null : valeur;
            }
        });
        module.addDeserializer(LocalDate.class, new CustomLocalDateDeserializer());
        module.addSerializer(LocalDate.class, new CustomLocalDateSerializer());
        mapper.registerModule(module);

        return mapper;
    }
}