package com.tn.arabsoft.remboursement_frais_medicaux.projections;

/** Effectifs du filtre Sexe d'une fenêtre de recherche : toutes les lignes, hommes, femmes. */
public interface ComptesSexeProjection {

    Long getTous();

    Long getM();

    Long getF();

}
