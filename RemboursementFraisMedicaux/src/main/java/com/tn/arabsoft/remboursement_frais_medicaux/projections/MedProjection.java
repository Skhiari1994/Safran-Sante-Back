package com.tn.arabsoft.remboursement_frais_medicaux.projections;

public interface MedProjection {

     String getLib_med();

     String getCod_med();

     Double getMdc_prix();

     Double getMed_prix();

     Double getPrix_remb();

     String getAbrv_act();
}
