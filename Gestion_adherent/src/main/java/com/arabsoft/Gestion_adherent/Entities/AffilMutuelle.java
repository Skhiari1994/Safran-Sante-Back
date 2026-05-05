package com.arabsoft.gestion_adherent.entities;

import com.arabsoft.gestion_adherent.configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestion_adherent.entities.cle.CleAffilMutuelle;
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
@Table(name = "affil_mutuelle")
@IdClass(CleAffilMutuelle.class)
@SuppressWarnings({ "java:S116" })
public class AffilMutuelle {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_ass;

    private String num_assur;

    private String typ_aff;

    private LocalDate dat_dem;

    private String obs_aff;

    private Long coef_cot;

    private String etat_aff;

    private String corps;

    private String cod_typ_depart;

    private String cod_affect;

}
