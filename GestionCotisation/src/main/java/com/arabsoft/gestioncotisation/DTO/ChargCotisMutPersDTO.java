package com.arabsoft.gestioncotisation.DTO;

import com.arabsoft.gestioncotisation.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ChargCotisMutPersDTO {
    private String soc;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate mois;
    private String mat;
}
