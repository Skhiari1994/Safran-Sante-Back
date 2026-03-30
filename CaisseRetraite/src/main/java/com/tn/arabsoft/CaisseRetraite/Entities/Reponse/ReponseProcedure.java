package com.tn.arabsoft.CaisseRetraite.Entities.Reponse;


import lombok.Data;


public class ReponseProcedure {
    private String message;

    public ReponseProcedure() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
