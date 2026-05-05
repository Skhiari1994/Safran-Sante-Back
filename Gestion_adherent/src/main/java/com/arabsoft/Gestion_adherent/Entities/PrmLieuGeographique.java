package com.arabsoft.gestion_adherent.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "prm_lieu_geographique")
@SuppressWarnings({ "java:S116" })
public class PrmLieuGeographique {

    @Id
    private String cod_lieu_geog;

    private String lib_lieu;

    private String lib_lieu_a;

    private String type_lieu;

}
