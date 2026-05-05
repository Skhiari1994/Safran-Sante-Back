package com.arabsoft.gestion_adherent.entities;

import com.arabsoft.gestion_adherent.configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestion_adherent.entities.cle.CleFamille;
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
@Table(name = "famille")
@IdClass(CleFamille.class)
@SuppressWarnings({ "java:S116" })
public class Famille {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private Long num_fam;

    private String parente;

    private String nom_pren;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_naiss;

    private String sexe;

    private String cod_sit;

    private String handicap;

    private String cod_activite;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_dece;

    private String pec;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_pec;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_mar;

    private String nom_jf;

    private String num_ass_conj;

    private String mat_pers_conj;

    private String pec_mut;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_pec_mut;

}
