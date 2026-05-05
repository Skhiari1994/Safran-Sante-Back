
package com.tn.arabsoft.remboursement_frais_medicaux.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BulletinDTO {

    @JsonProperty("mat_pers")
    private String matPers;

    @JsonProperty("num_fam")
    private String numFam;

    @JsonProperty("dat_soin")
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)

    private LocalDate datSoin;
    @JsonProperty("envoi")

    private String choix;
    @JsonProperty("mnt_honor")
    private double mntHonor;

    @JsonProperty("mnt_net")
    private double mntNet;

    @JsonProperty("ord_bult")
    private int ordBult;
    // Getters and setters
}
