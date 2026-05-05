package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigPhar;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "lig_phar")
@IdClass(CleLigPhar.class)
@SuppressWarnings({ "java:S116" })
public class LigPhar {
    @Id
    private String cod_soc;
    @Id
    private String mat_pers;
    @Id
    private Long num_fam;
    @Id
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_soin;
    @Id
    private String cod_med;
    @Id
    private Long num_lig;
    private Long indice;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_act;
    private String prf_typ;
    private String prf_cod;
    private BigDecimal mnt_honor;
    private BigDecimal mnt_net;
    private BigDecimal mnt_remb;
    private BigDecimal mdc_prix;
    private BigDecimal med_prix;
    private BigDecimal prix_remb;
    private String obs;
    private String obs_a;
    private Long nbr_piece;
    private Long nbr_vign;
    private String abrv_act;

}