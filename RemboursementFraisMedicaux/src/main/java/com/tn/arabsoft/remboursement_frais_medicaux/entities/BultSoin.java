package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.BultSoinCle;

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
@Table(name = "bult_soin")
@IdClass(BultSoinCle.class)
@SuppressWarnings({ "java:S116" })
public class BultSoin {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private String num_fam;

    @Id
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_soin;

    private String cod_bord;

    private String cod_assur;

    private String num_soin;

    private BigDecimal ord_bult;

    private BigDecimal tot_honor;

    private BigDecimal tot_net;

    private BigDecimal tot_remb;

    private String reg_remb;

    private String cod_malad;

    private String num_pec;

    private String cod_fil;

    private String num_assur;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_prev_accouch;

    private String nat_bult;

    private String mat_pers_conj;

    private String num_ass_conj;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_saisie;

    private String obs;

    private String obs_a;

    private String envoi;

    private String ann_plaf_imp;

    private String reg_adh;

    private String mod_pay;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_vir;

}
