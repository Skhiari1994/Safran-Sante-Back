package com.arabsoft.gestion_adherent.dto;

public class ResponseProcedureCharge {

    private String message;

    private int lignesTransferees; // Nombre de lignes transférées

    // getters & setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLignesTransferees() {
        return lignesTransferees;
    }

    public void setLignesTransferees(int lignesTransferees) {
        this.lignesTransferees = lignesTransferees;
    }

}
