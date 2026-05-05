package com.tn.arabsoft.remboursement_frais_medicaux.entities.cle;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings({ "java:S116" })
public class CleLigBultArriver implements Serializable {

    private String cod_soc;

    private String mat_pers;

    private Long num_fam;

    private LocalDate dat_soin;

}
