package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigBultCle;

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
@Table(name = "lig_bult")
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
    private BigDecimal mnt_honor;
    private BigDecimal mnt_net;
    private BigDecimal mnt_remb;
    private String obs;
    private String obs_a;
    private Long nbr_piece;
    private Long nbr_vign;
    private String nat_act;
    private Long mtt_acte;
    private BigDecimal taux_act;
    private String plafonne;
    private BigDecimal plafond;
    private String a_indice;
    private String ctr_duree;
    private Long duree_act;
    private String imput_plaf;

}
