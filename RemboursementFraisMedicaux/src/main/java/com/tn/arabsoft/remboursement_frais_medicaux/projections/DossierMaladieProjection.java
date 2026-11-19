package com.tn.arabsoft.remboursement_frais_medicaux.projections;

/** Maladie d'un dossier maladie (DOSSIER_MLD + MALADIE) : code, libellé et numéro du dossier. */
@SuppressWarnings({ "java:S100" })
public interface DossierMaladieProjection {

    String getcod_malad();

    String getlib_malad();

    String getnum_dos_mld();

}
