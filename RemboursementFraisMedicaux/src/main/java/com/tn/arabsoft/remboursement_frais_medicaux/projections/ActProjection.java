package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.math.BigDecimal;

@SuppressWarnings({ "java:S100" })
public interface ActProjection {

    String getLib_act();

    String getCod_act();

    String getLet_cod();

    BigDecimal getCot_act();

    BigDecimal getAct_prix();

    String getAbrv_act();

    BigDecimal getTaux_act();

}
