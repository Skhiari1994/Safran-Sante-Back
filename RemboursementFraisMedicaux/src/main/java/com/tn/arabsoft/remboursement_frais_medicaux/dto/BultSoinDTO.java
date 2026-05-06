package com.tn.arabsoft.remboursement_frais_medicaux.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BultSoinDTO {

    private String codSoc;

    private String matPers;

    private String numFam;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate datSoin;

    private String codBord;

    private String codAssur;

    private String numSoin;

    private Long ordBult;

    private BigDecimal totHonor;

    private BigDecimal totNet;

    private BigDecimal totRemb;

    private BigDecimal totRembMed;

    private String regRemb;

    private String codMalad;

    private String numPec;

    private String codFil;

    private String numAssur;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate datPrevAccouch;

    private String natBult;

    private String matPersConj;

    private String numAssConj;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate datSaisie;

    private String obs;

    private String obsA;

    private String envoi;

    private String annPlafImp;

    private String regAdh;

    private String modPay;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate datVir;

    private String matInt;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate datNais;

    // Extra fields from projection
    private String reclam;

    private String typBult;

    private String numSoinCnam;

    private String decisMed;

    private String choix;

    private String numRetr;

    private String nomCompletPers;

    private String libAssur;

    private String libFill;

    private String libRemb;

    private String nomAdherent;

}