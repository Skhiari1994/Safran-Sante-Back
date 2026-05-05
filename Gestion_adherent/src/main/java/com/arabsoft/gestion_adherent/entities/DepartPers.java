package com.arabsoft.gestion_adherent.entities;

import com.arabsoft.gestion_adherent.configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestion_adherent.entities.cle.CleDepartPers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "depart_pers")
@IdClass(CleDepartPers.class)
@SuppressWarnings({ "java:S116" })
public class DepartPers {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    private String cod_typ_depart;

    @Id
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_depart;

    private LocalDate dat_sais_depart;

    private String obs_depart;

    private String etat_depart;

    private String corps;

    private String cod_affect;

    private String cod_lieu_geog;

}
