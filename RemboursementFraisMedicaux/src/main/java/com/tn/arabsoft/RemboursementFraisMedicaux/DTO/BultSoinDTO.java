package com.tn.arabsoft.RemboursementFraisMedicaux.DTO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.RemboursementFraisMedicaux.Configuration.CustomLocalDateDeserializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BultSoinDTO {

    // Composite key fields
    private String codSoc;
    private String matPers;
    private String numFam;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate datSoin;

    // Basic info
    private String codBord;
    private String codAssur;
    private String numSoin;
    private Long ordBult;

    // Monetary fields
    private BigDecimal totHonor;
    private BigDecimal totNet;
    private BigDecimal totRemb;
    private BigDecimal totRembMed;

    // Other info
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

    // Getters and Setters (or use Lombok @Data)
    // --- Example ---
    public String getCodSoc() { return codSoc; }
    public void setCodSoc(String codSoc) { this.codSoc = codSoc; }

    public String getMatPers() { return matPers; }
    public void setMatPers(String matPers) { this.matPers = matPers; }

    public String getNumFam() { return numFam; }
    public void setNumFam(String numFam) { this.numFam = numFam; }

    public LocalDate getDatSoin() { return datSoin; }
    public void setDatSoin(LocalDate datSoin) { this.datSoin = datSoin; }

    public BigDecimal getTotRemb() { return totRemb; }
    public void setTotRemb(BigDecimal totRemb) { this.totRemb = totRemb; }

    // ... continue for all fields
}
