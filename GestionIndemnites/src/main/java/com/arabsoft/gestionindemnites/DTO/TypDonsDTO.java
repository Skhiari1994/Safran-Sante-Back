package com.arabsoft.gestionindemnites.DTO;

import com.arabsoft.gestionindemnites.Configuration.CustomLocalDateDeserializer;
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
public class TypDonsDTO {

    private String natDon;
    private String codAffect;
    private String typDon;
    private String codSoc;
    private String matPers;
    private Long numFam;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate datDemDon;

}
