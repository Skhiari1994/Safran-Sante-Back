package com.arabsoft.gestion_adherent.entities;

import com.arabsoft.gestion_adherent.configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestion_adherent.entities.cle.CleCarteSoinsPers;
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
@Table(name = "carte_soins_pers")
@IdClass(CleCarteSoinsPers.class)
@SuppressWarnings({ "java:S116" })
public class CarteSoinsPers {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private String num_fam;

    @Id
    private String nat_cart;

    @Id
    private String num_cart;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_cart;

    private String etat_cart;

    private String obs_cart;

    private String perdu;

}
