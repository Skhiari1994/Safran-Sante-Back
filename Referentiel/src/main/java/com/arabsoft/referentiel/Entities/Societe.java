package com.arabsoft.referentiel.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Formula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "societe")
@ToString
@SuppressWarnings({ "java:S116" })
public class Societe {

    @Id
    private String cod_soc;

    private String lib_soc;

    private String lib_soc_a;

    private String adr_soc;

    private String adr_soc_a;

    private String cod_retr;

    private String tel_soc;

    private String fax_soc;

    private String num_retr;

    private String rep_web;

    private String regime;

    private String num_police;

    /**
     * 1 si la société a un logo (colonne LOGO_SOC), sinon 0. L'image elle-même n'est jamais chargée avec la société
     * (liste légère) : elle se lit et s'écrit par les services /societe/{codSoc}/logo.
     */
    @Formula("(CASE WHEN logo_soc IS NULL THEN 0 ELSE 1 END)")
    private Integer a_logo;

}
