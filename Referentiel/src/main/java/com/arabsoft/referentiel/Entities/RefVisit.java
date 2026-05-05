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
@Table(name = "ref_visit")
@ToString
@SuppressWarnings({ "java:S116" })
public class RefVisit {

    @Id
    private String cod_visit;

    private String lib_visit;

    private String lib_visit_a;

    private String cod_sec;

    private String typ_nat;

    private String abrv_act;

    private BigDecimal prix_visit;

    private BigDecimal taux_remb;

}
