package com.tn.arabsoft.remboursement_frais_medicaux.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;

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
public class UpdateBultSoinDTO {

    private String cod_soc;
    private String mat_pers;
    private String num_fam;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_soin;
    private String envoi;

}
