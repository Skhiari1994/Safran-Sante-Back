package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigBultApp;

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
@Table(name = "lig_bult_app")
@IdClass(CleLigBultApp.class)
@SuppressWarnings({ "java:S116" })
public class LigBultApp {

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
    private String abrv_act;

    @Id
    private String cod_app;

    @Id
    private String num_lig_app;

    private Long num_lig;

    private BigDecimal mnt_honor;

    private BigDecimal mnt_net;

    private BigDecimal mnt_remb;

    private String accord_app;

    private Long indice;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_act;

    private String prf_typ;

    private String prf_cod;

    private String num_pec_app;

    private BigDecimal mut_mnt_net;

}