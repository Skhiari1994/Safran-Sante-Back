package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.Data;

import java.time.LocalDate;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleDossierMld;

@Data
@Entity
@Table(name = "dossier_mld")
@IdClass(CleDossierMld.class)
@SuppressWarnings({ "java:S116" })
public class DossierMld {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private String num_dos_mld;

    private String cod_malad;

    private LocalDate dat_doss_mld;

    private String etat_dos_mld;

    private Long num_fam;

}
