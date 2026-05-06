package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface FamillePersonnelBultSoin {

    String getNum_fam();

    String getNom();

    LocalDate getDat_naiss();

}
