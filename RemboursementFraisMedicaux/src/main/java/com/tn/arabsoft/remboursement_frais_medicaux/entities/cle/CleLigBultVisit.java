package com.tn.arabsoft.remboursement_frais_medicaux.entities.cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@SuppressWarnings({ "java:S116" })
public class CleLigBultVisit implements Serializable {

    private String cod_soc;

    private String mat_pers;

    private String num_fam;

    private LocalDate dat_soin;

    private String abrv_act;

    private String cod_visit;

    private LocalDate dat_act;

}
