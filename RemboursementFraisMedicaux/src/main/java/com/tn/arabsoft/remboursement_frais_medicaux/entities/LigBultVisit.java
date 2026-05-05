package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigBultVisit;

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
@Table(name = "lig_bult_visit")
@IdClass(CleLigBultVisit.class)
@SuppressWarnings({ "java:S116" })
public class LigBultVisit {

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

    @Id
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_act;

    @Id
    private String cod_visit;

    private Long num_lig;

    private BigDecimal mnt_honor;

    private BigDecimal mnt_remb;

    private BigDecimal mnt_net;

    private Long indice;

    private String prf_typ;

    private String prf_cod;

    private BigDecimal prix_visit;

    private BigDecimal taux_remb;

    private BigDecimal mut_mnt_net;

}