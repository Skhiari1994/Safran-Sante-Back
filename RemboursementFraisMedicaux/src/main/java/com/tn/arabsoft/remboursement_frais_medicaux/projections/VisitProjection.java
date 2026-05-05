package com.tn.arabsoft.remboursement_frais_medicaux.projections;

public interface VisitProjection {
    String getCod_Visit();

    String getLib_Visit();

    String getAbrv_Act();

    Double getPrix_Visit();

    Double getTaux_Remb();
}
