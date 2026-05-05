package com.arabsoft.referentiel.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ref_appareil")
@ToString
@SuppressWarnings({ "java:S116" })
public class RefAppareil {

    @Id
    private String cod_app;

    private String lib_app;

    private String lib_app_a;

    private String abrv_act;

    private BigDecimal prix_app;

    private String mois;

    private String devis;

    private Long duree_app;

}
