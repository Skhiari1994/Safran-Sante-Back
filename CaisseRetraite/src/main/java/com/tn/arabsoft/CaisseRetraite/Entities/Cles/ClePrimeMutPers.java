package com.tn.arabsoft.CaisseRetraite.Entities.Cles;

import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
public class ClePrimeMutPers implements Serializable {

    private String cod_soc;
    private String  mat_pers;
    private Long  num_prime;


//    public ClePrimeMutPers(String cod_soc, String mat_pers, Long num_prime) {
//        this.cod_soc = cod_soc;
//        this.mat_pers = mat_pers;
//        this.num_prime = num_prime;
//    }
}
