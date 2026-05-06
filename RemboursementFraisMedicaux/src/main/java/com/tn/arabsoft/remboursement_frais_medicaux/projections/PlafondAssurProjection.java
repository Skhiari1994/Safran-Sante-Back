package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.math.BigDecimal;

@SuppressWarnings({ "java:S100" })
public interface PlafondAssurProjection {

    Long getAnnee_assur();

    String getCod_soc();

    String getMat_pers();

    String getCod_assur();

    BigDecimal getPlafond();

    BigDecimal getSold_plaf();

    BigDecimal getSold_assur_estim();

    String getLibAssur();
}
