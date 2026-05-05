package com.arabsoft.gestion_adherent.entities;

import com.arabsoft.gestion_adherent.entities.cle.CleAgence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "agence")
@IdClass(CleAgence.class)
@SuppressWarnings({ "java:S116" })
public class Agence {

    @Id
    private String cod_banq;

    @Id
    private String cod_agc;

    private String lib_agc;

    private String lib_agc_a;

    private String adr_agc;

    private String adr_agc_a;

    private String tel_agc;

    private String tel2_agc;

    private String fax_agc;

    private String cod_banq_banq;

    private String cod_agc_agc;

}