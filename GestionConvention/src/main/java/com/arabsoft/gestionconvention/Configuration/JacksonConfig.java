package com.arabsoft.gestionconvention.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;


@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Register JSR310 module to handle Java 8 date/time classes
        mapper.registerModule(new JavaTimeModule());

        // Register custom serializer and deserializer for LocalDate
        SimpleModule module = new SimpleModule();
        module.addDeserializer(LocalDate.class, new CustomLocalDateDeserializer());
        module.addSerializer(LocalDate.class, new CustomLocalDateSerializer());
        mapper.registerModule(module);

        return mapper;
    }
}