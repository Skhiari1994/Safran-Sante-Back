package com.tn.arabsoft.remboursement_frais_medicaux.projections;

public interface LigPharProjection {

    String getLIB_MED();

    String getCOD_MED();

    Double getMDC_PRIX();

    Double getMED_PRIX();

    Double getPRIX_REMB();

    String getABRV_ACT();
}
