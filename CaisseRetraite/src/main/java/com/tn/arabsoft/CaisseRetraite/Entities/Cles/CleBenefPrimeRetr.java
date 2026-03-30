package com.tn.arabsoft.CaisseRetraite.Entities.Cles;

import lombok.Data;

import java.io.Serializable;
@Data
public class CleBenefPrimeRetr implements Serializable {
    private String cod_soc;
    private String   mat_pers;
    private Long num_remb;
    private Long  num_fam;
}
