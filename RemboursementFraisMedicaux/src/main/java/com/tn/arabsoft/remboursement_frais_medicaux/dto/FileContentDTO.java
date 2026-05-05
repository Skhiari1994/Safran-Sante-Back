package com.tn.arabsoft.remboursement_frais_medicaux.dto;

public class FileContentDTO {
    private String[] lines;
    private String codSoc;

    public String[] getLines() {
        return lines;
    }

    public void setLines(String[] lines) {
        this.lines = lines;
    }

    public String getCodSoc() {
        return codSoc;
    }

    public void setCodSoc(String codSoc) {
        this.codSoc = codSoc;
    }
}