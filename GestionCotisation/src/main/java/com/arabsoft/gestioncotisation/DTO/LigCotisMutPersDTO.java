package com.arabsoft.gestioncotisation.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
public class LigCotisMutPersDTO{
    private String codSoc;
        private String matPers;
        private String numCot;
        private LocalDate datMut;
        private BigDecimal mntPayer;
        private LocalDate datCot;

        public LigCotisMutPersDTO(String codSoc, String matPers, String numCot, LocalDate datMut, BigDecimal mntPayer, LocalDate datCot) {
            this.codSoc = codSoc;
            this.matPers = matPers;
            this.numCot = numCot;
            this.datMut = datMut;
            this.mntPayer = mntPayer;
            this.datCot = datCot;
        }

        // Getters and Setters
}