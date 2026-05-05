package com.tn.arabsoft.remboursement_frais_medicaux.entities.cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@SuppressWarnings({ "java:S116" })
public class ClePersAffil implements Serializable {

    private String cod_soc;

    private String mat_pers;

    private Long annee_aff;

}
