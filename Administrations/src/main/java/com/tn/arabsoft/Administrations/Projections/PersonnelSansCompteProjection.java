package com.tn.arabsoft.administrations.projections;

/** Membre du personnel qui n'a pas encore de compte utilisateur (liste de choix de la fenetre d'ajout). */
public interface PersonnelSansCompteProjection {

    String getMatPers();

    String getCin();

    /** Date de naissance au format yyyy-MM-dd. */
    String getDatNais();

    String getFullName();

}
