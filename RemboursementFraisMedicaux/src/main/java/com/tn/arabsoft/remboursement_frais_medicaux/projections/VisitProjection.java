package com.tn.arabsoft.remboursement_frais_medicaux.projections;

@SuppressWarnings({ "java:S100" })
public interface VisitProjection {

    String getCod_Visit();

    String getLib_Visit();

    String getAbrv_Act();

    Double getPrix_Visit();

    Double getTaux_Remb();

}
