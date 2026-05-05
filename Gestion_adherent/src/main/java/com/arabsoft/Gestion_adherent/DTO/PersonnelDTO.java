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
public class PersonnelDTO {

    private String cod_soc;

    private String mat_pers;

    private String cod_assur;

    private String nom_pers;

    private String nom_pers_a;

    private String pren_pers;

    private String pren_pers_a;

    private String nom_jf;

    private String nom_jf_a;

    private String sexe;

    private String cin;

    private LocalDate dat_nais;

    private LocalDate dat_emb;

    private String cod_sit;

    private Long nbr_enf;

    private String cod_retr;

    private String num_retr;

    private String num_assur;

    private LocalDate dat_ass;

    private String cod_pay;

    private String rib;

    private String cod_natp;

    private String cod_banq;

    private String cod_agc;

    private LocalDate dat_dece;

    private String etat_act;

    private LocalDate dat_motif;

    private String cod_lieu_geog;

    private Long bas_plafond;

    private Long photo_pers;

    private String lieu_nais;

    private Long mnt_param;

    private String etat_prof;

    private LocalDate dat_aff_cnam;

    private String corps;

    private String cod_affect;

    private LocalDate dat_affect;

    private String cod_typ_depart;

    private LocalDate dat_depart;

    private String typ_aff;

    private String cod_user;

    private LocalDate dat_maj;

    private String mat_int;

    private String pers_carte;

    private String fullName;

}
