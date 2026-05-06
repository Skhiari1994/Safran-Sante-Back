package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.ClePlafondAssur;

@Data
@Entity
@Table(name = "plafond_assur")
@IdClass(ClePlafondAssur.class)
@SuppressWarnings({ "java:S116" })
public class PlafondAssur {

    @Id
    private Long annee_assur;

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    private String cod_assur;

    private BigDecimal plafond;

    private BigDecimal sold_plaf;

    private BigDecimal sold_assur_estim;

}
