package com.arabsoft.gestionindemnites.DTO;

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
public class ChekFamProcScolDTO {    private String natDon;
    private String typDon;
    private String codSoc;
    private String matPers;
    private Long numFam;
    private LocalDate datDemDon;
    private String codAffect;

    // Getters et Setters
}
