package com.arabsoft.referentiel.entities;

import com.arabsoft.referentiel.configurations.CustomLocalDateDeserializer;
import com.arabsoft.referentiel.entities.cle.LigBultCle;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lig_bult")
@ToString
@IdClass(LigBultCle.class)
@SuppressWarnings({ "java:S116" })
public class LigBult {
    @Id
    private String cod_soc;
    @Id
    private String mat_pers;
    @Id
    private String num_fam;
    @Id
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_soin;
    @Id
    private String abrv_act;
    private Long num_lig;
    private String prf_typ;
    private String prf_cod;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_act;
    private Long indice;
    private Long mnt_honor;
    private Long mnt_net;
    private Long mnt_remb;
    private String obs;
    private String obs_a;
    private Long nbr_piece;
    private Long nbr_vign;
    private String nat_act;
    private Long mtt_acte;
    private Long taux_act;
    private String plafonne;
    private Long plafond;
    private String a_indice;
    private String ctr_duree;
    private Long duree_act;
    private String imput_plaf;

}
