package com.arabsoft.gestion_adherent.dto;

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
@SuppressWarnings({ "java:S116" })
public class FamilleDTO {

    private String cod_soc;

    private String mat_pers;

    private Long num_fam;

    private String parente;

    private String nom_pren;

    private LocalDate dat_naiss;

    private String sexe;

    private String cod_sit;

    private String handicap;

    private String cod_activite;

    private LocalDate dat_dece;

    private String pec;

    private LocalDate dat_pec;

    private LocalDate dat_mar;

    private String nom_jf;

    private String num_ass_conj;

    private String mat_pers_conj;

    private String pec_mut;

    private LocalDate dat_pec_mut;

}
