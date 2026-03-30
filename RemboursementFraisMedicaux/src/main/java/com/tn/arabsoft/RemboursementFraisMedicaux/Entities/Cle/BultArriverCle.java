package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BultArriverCle implements Serializable {

   private String cod_soc;
   private String mat_pers;
   private Integer num_fam;
   private LocalDate dat_soin;


}
