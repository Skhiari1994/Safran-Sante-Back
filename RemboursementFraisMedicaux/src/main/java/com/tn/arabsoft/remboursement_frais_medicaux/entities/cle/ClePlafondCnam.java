package com.tn.arabsoft.remboursement_frais_medicaux.entities.cle;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings({ "java:S116" })
public class ClePlafondCnam implements Serializable {

    private Long annee_cnam;

    private String cod_soc;

    private String mat_pers;

}
