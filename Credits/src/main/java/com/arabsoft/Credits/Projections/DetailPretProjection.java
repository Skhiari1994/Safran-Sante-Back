package com.arabsoft.Credits.Projections;

public interface DetailPretProjection {

    String getCodGrpPret(); // p.cod_grp_pret
    String getTypPret();    // p.typ_pret
    String getLibPret();    // t.lib_pret
    Integer getNbrTranche(); // t.nbr_tranche
    Double getPlafond();     // t.plafond
    Double getTauxInt();     // NVL(t.taux_int, 0)
    Integer getDureeRemb();  // NVL(t.duree_remb, 0)
    Integer getDelaiGrace(); // NVL(t.delai_grace, 0)
}
