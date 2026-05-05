package com.tn.arabsoft.remboursement_frais_medicaux.dto;

public class CnamFichRequest {
    private String codSoc;
    private String codBord;

    public String getCodSoc() {
        return codSoc;
    }

    public void setCodSoc(String codSoc) {
        this.codSoc = codSoc;
    }

    public String getCodBord() {
        return codBord;
    }

    public void setCodBord(String codBord) {
        this.codBord = codBord;
    }
}