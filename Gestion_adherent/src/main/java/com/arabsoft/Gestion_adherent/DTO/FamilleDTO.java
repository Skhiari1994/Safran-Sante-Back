package com.arabsoft.Gestion_adherent.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
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
