package com.arabsoft.gestioncotisation.DTO;

import com.arabsoft.gestioncotisation.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDate;
@Data
public class CotisAddDTO {

        private String cod_soc;
        private String mat_pers;
        @JsonDeserialize(using = CustomLocalDateDeserializer.class)
        private LocalDate dat_deb;
        @JsonDeserialize(using = CustomLocalDateDeserializer.class)
        private LocalDate dat_fin;

        // Getters and Setters

}
