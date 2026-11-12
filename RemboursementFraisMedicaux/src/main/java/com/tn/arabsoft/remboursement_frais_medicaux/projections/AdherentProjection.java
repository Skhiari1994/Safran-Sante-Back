package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface AdherentProjection {

    Integer getNum_fam();

    String getNom_pren();

    LocalDate getDat_nais();

}
