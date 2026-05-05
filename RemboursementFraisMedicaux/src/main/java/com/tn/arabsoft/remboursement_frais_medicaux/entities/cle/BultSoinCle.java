package com.tn.arabsoft.remboursement_frais_medicaux.entities.cle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings({ "java:S116" })
public class BultSoinCle {

    private String cod_soc;

    private String mat_pers;

    private String num_fam;

    private LocalDate dat_soin;

}
