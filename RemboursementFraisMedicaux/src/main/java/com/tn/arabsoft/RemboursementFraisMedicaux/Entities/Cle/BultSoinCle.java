package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.RemboursementFraisMedicaux.Configuration.CustomLocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BultSoinCle implements Serializable {

    private String cod_soc;
    private String mat_pers;
    private String num_fam;
    private LocalDate dat_soin;

}
