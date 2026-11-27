package com.arabsoft.gestion_adherent.projections;

/** Effectifs du filtre Sexe d'une fenêtre de recherche : toutes les lignes, hommes, femmes. */
public interface ComptesSexeProjection {

    Long getTous();

    Long getM();

    Long getF();

}
