package com.arabsoft.referentiel.entities;

import com.arabsoft.referentiel.configurations.CustomLocalDateDeserializer;
import com.arabsoft.referentiel.entities.cle.BultSoinCle;
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
@Table(name = "bult_soin")
@ToString
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

    @Id
    private String cod_bord;

    private String cod_assur;

    private String num_soin;

    private Long ord_bult;

    private Long tot_honor;

    private Long tot_net;

    private Long tot_remb;

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
