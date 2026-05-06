package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.ClePriseCharge;

@Entity
@Data
@Table(name = "prise_charge")
@IdClass(ClePriseCharge.class)
@SuppressWarnings({ "java:S116" })
public class PriseCharge {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private String num_pec;

    private LocalDate dat_pec;

    private Long num_fam;

    private String etat_pec;

    private String prf_typ;

    private String prf_cod;

    private BigDecimal mnt_pec;

    private BigDecimal mnt_remb;

    private LocalDate dat_eff;

}
