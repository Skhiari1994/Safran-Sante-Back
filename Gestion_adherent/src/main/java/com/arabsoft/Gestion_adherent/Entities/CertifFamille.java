package com.arabsoft.gestion_adherent.entities;

import com.arabsoft.gestion_adherent.configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestion_adherent.entities.cle.CleCertifFamille;
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
@Table(name = "certif_famille")
@IdClass(CleCertifFamille.class)
@SuppressWarnings({ "java:S116" })
public class CertifFamille {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private Long num_fam;

    @Id
    private String annee_certif;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_certif;

    private String annee_scol;

    private String obs;

}
