package com.tn.arabsoft.RemboursementFraisMedicaux.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.RemboursementFraisMedicaux.Configuration.CustomLocalDateDeserializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
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
