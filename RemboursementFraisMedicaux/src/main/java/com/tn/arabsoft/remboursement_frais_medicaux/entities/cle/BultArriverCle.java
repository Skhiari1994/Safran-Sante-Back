package com.tn.arabsoft.remboursement_frais_medicaux.entities.cle;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuppressWarnings({ "java:S116" })
public class BultArriverCle {

   private String cod_soc;

   private String mat_pers;

   private Integer num_fam;

   private LocalDate dat_soin;

}
