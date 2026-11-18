package com.tn.arabsoft.remboursement_frais_medicaux;

public enum ControleBareme {

    INDICE("a_indice"),
    PIECE("verif_piece"),
    VIGNETTE("verif_vign");

    private final String colonne;

    ControleBareme(String colonne) {
        this.colonne = colonne;
    }

    public String getColonne() {
        return colonne;
    }

}
