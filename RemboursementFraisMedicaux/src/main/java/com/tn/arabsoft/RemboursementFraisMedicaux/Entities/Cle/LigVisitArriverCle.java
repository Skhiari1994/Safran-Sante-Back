package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LigVisitArriverCle implements Serializable {

    private String cod_soc;
    private String mat_pers;
    private Long num_fam;
    private LocalDate dat_soin;
    private Long num_lig;
}
