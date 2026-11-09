package com.arabsoft.gestion_adherent.entities;

import com.arabsoft.gestion_adherent.configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestion_adherent.entities.cle.CleCarteSoinsCnam;
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
@Table(name = "carte_soins_cnam")
@IdClass(CleCarteSoinsCnam.class)
@SuppressWarnings({ "java:S116" })
public class CarteSoinsCnam {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private String nat_cart;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_cart;

    @Id
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate date_debut;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate date_fin;

    private String etat_cart;

    private String obs_cart;

    private String perdu;

}
