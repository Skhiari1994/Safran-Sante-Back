package com.arabsoft.gestion_adherent.entities.cle;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings({ "java:S116" })
public class CleCertifFamille {

    private String cod_soc;

    private String mat_pers;

    private Long num_fam;

    private String annee_certif;

}