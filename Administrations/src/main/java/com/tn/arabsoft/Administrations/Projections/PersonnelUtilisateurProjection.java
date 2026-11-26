package com.tn.arabsoft.administrations.projections;

/** Fiche du personnel (PERSONNEL + e-mail de ADR_PERS) reprise pour creer un compte utilisateur (ADMUSER). */
public interface PersonnelUtilisateurProjection {

    String getCodSoc();

    String getMatricule();

    String getNom();

    String getPrenom();

    String getCin();

    String getEmail();

    /** Nombre de comptes deja crees pour ce matricule dans cette societe (0 ou 1). */
    Integer getDejaUtilisateur();

}
