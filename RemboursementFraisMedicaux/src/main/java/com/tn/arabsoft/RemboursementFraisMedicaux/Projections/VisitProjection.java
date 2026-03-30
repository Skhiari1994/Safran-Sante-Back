package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;

public interface VisitProjection {
    String getCod_Visit();
    String getLib_Visit();
    String getAbrv_Act();
    Double getPrix_Visit();
    Double getTaux_Remb();
}
