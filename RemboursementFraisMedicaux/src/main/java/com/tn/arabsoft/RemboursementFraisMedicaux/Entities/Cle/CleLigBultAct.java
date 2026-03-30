package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class CleLigBultAct implements Serializable {

    private String cod_soc;
    private String    mat_pers;
    private Long   num_fam;
    private LocalDate dat_soin;
    private String  abrv_act;
    private String cod_act;
    private Long  num_lig	;
    private LocalDate  dat_act;
}
