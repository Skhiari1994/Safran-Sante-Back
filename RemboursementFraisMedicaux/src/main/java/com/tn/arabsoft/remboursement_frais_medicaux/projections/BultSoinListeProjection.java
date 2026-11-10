package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.time.LocalDate;

/**
 * Version légère d'un bulletin pour les listes de recherche (fenêtre « Sélectionnez une valeur ») : 5 champs au lieu
 * d'environ 40. La fiche complète est chargée ensuite, uniquement pour la ligne choisie.
 */
@SuppressWarnings({ "java:S100" })
public interface BultSoinListeProjection {

    String getCod_soc();

    String getMat_pers();

    String getNum_fam();

    LocalDate getDat_soin();

    String getCod_bord();

}
