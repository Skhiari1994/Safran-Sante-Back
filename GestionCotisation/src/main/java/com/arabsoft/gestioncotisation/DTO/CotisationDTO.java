package com.arabsoft.gestioncotisation.DTO;

import com.arabsoft.gestioncotisation.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
public class CotisationDTO {
    private String codSoc;
    private String matPers;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate datDeb;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate datFin;

    // Getters and Setters
}
