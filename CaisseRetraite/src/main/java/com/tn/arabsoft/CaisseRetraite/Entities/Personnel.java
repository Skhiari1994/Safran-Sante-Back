package com.tn.arabsoft.CaisseRetraite.Entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.CaisseRetraite.Configurations.CustomLocalDateDeserializer;
import com.tn.arabsoft.CaisseRetraite.Entities.Reponse.ClePersonnel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@Entity(name="Personnel")
@IdClass(ClePersonnel.class)
public class Personnel {
    @Id
     private String cod_soc	;
    @Id
     private String  mat_pers;
    private String  cod_assur;
    private String  nom_pers;
    private String   sexe;
    private String   cin;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate  dat_nais;
    private String   pren_pers;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate  dat_emb;
    private String    cod_sit;
    private Long   nbr_enf;
    private String   cod_retr;
    private String   num_retr;
    private String   num_assur;
     private LocalDate dat_ass;
    private String   cod_pay;
    private String   rib;
    private String  nom_pers_a;
    private String  pren_pers_a;
    private String  cod_natp;
    private String  cod_banq;
    private String  cod_agc;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_dece;
    private String etat_act;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_motif;
    private String cod_lieu_geog;
    private Long bas_plafond;
    private String  nom_jf;
    private String  nom_jf_a;
    private byte[] photo_pers;
    private String  lieu_nais;
    private Long  mnt_param;
    private String  etat_prof;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate  dat_aff_cnam	;
    private String corps;
    private String cod_affect;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_affect;
    private String  cod_typ_depart;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_depart;
    private String  typ_aff;
    private String  cod_user;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_maj	;
    private String  mat_int;
    private String  pers_carte;

}
