package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;

import java.math.BigDecimal;

public interface ActProjection {
    String getLib_act();
    String getCod_act();
    String getLet_cod();
    BigDecimal getCot_act();
    BigDecimal getAct_prix();
    String getAbrv_act();
    BigDecimal getTaux_act();
}
