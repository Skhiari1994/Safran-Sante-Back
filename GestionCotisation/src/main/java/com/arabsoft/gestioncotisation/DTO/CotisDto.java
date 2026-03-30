package com.arabsoft.gestioncotisation.DTO;

import com.arabsoft.gestioncotisation.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CotisDto {


    private String cod_soc;

    private String mat_pers;

    private Long num_cot;
    private String typ_cot;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_deb;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_fin;

    private BigDecimal mnt_a_payer;
    private BigDecimal mnt_payer;
    private String mod_pay;
    private String ref_pay;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_saisie;

    private String etat_cot;
    private String corps;
    private String cod_typ_depart;
    private String cod_affect;
    private BigDecimal mnt_param;
    private String num_retr;
    private Long seq_ecrt1;   // Changed from float to Float
    private Long seq_ecrt2;   // Changed from float to Float
    private String imput;
}

