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
@Table(name = "ref_med")
@ToString
@SuppressWarnings({ "java:S116" })
public class RefMed {

    @Id
    private String cod_med;

    private String lib_med;

    private BigDecimal mdc_prix;

    private BigDecimal med_prix;

    private BigDecimal prix_remb;

    private String cat_med;

    private String ap;

    private String rb;

    private String abrv_act;

}
