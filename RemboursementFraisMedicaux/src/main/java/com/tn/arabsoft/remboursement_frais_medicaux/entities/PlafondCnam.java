package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.ClePlafondCnam;

@Data
@Entity
@Table(name = "plafond_cnam")
@IdClass(ClePlafondCnam.class)
@SuppressWarnings({ "java:S116" })
public class PlafondCnam {

    @Id
    private Long annee_cnam;

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    private BigDecimal plafond;

    private BigDecimal sold_plaf;

    private BigDecimal sold_cnam_estim;

}
