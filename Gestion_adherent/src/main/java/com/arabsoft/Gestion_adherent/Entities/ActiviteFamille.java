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
@Table(name = "activite_famille")
@SuppressWarnings({ "java:S116" })
public class ActiviteFamille {

    @Id
    private String cod_activite;

    private String lib_activite;

    private String lib_activite_a;

    private String parente_act;

}
