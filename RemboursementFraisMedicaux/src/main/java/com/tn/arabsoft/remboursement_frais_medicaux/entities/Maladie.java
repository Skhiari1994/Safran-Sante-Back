package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "maladie")
@SuppressWarnings({ "java:S116" })
public class Maladie {

    @Id
    private String cod_malad;

    private String lib_malad;

    private String lib_malad_a;

    private String apci;

    private BigDecimal mnt_seuil;

}
