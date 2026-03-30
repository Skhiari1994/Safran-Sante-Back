package com.tn.arabsoft.RemboursementFraisMedicaux.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.RemboursementFraisMedicaux.Configuration.CustomLocalDateDeserializer;
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
public class UpdateBultSoinDTO {

    private String cod_soc;
    private String mat_pers;
    private String num_fam;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_soin;
    private String envoi;

}
