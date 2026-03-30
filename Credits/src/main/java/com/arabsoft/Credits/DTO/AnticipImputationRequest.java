package com.arabsoft.Credits.DTO;

import java.math.BigDecimal;

public class AnticipImputationRequest {

    private String codSoc;
    private String matPers;
    private String numVir;
    private String refMetier;
    private BigDecimal montVir;
    private BigDecimal mntEsp;
    private BigDecimal restVir;

    // Constructors
    public AnticipImputationRequest() {
    }

    public AnticipImputationRequest(String codSoc, String matPers, String numVir,
                                    String refMetier, BigDecimal montVir,
                                    BigDecimal mntEsp, BigDecimal restVir) {
        this.codSoc = codSoc;
        this.matPers = matPers;
        this.numVir = numVir;
        this.refMetier = refMetier;
        this.montVir = montVir;
        this.mntEsp = mntEsp;
        this.restVir = restVir;
    }

    // Getters and Setters
    public String getCodSoc() {
        return codSoc;
    }

    public void setCodSoc(String codSoc) {
        this.codSoc = codSoc;
    }

    public String getMatPers() {
        return matPers;
    }

    public void setMatPers(String matPers) {
        this.matPers = matPers;
    }

    public String getNumVir() {
        return numVir;
    }

    public void setNumVir(String numVir) {
        this.numVir = numVir;
    }

    public String getRefMetier() {
        return refMetier;
    }

    public void setRefMetier(String refMetier) {
        this.refMetier = refMetier;
    }

    public BigDecimal getMontVir() {
        return montVir;
    }

    public void setMontVir(BigDecimal montVir) {
        this.montVir = montVir;
    }

    public BigDecimal getMntEsp() {
        return mntEsp;
    }

    public void setMntEsp(BigDecimal mntEsp) {
        this.mntEsp = mntEsp;
    }

    public BigDecimal getRestVir() {
        return restVir;
    }

    public void setRestVir(BigDecimal restVir) {
        this.restVir = restVir;
    }

    @Override
    public String toString() {
        return "AnticipImputationRequest{" +
                "codSoc='" + codSoc + '\'' +
                ", matPers='" + matPers + '\'' +
                ", numVir='" + numVir + '\'' +
                ", refMetier='" + refMetier + '\'' +
                ", montVir=" + montVir +
                ", mntEsp=" + mntEsp +
                ", restVir=" + restVir +
                '}';
    }
}