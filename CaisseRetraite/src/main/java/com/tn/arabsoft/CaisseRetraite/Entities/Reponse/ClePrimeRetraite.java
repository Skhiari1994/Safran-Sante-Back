package com.tn.arabsoft.CaisseRetraite.Entities.Reponse;

import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class ClePrimeRetraite implements Serializable {

    private String cod_soc;
    private String  mat_pers;
    private Long  num_remb;


}
