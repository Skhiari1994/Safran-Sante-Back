package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;

import jakarta.persistence.Id;

import java.math.BigDecimal;

public interface PlafondAssurProjection {

    Long getAnnee_assur();
     String  getCod_soc();
     String   getMat_pers();
     String   getCod_assur();
     BigDecimal getPlafond();
     BigDecimal getSold_plaf();
     BigDecimal getSold_assur_estim();
     String getLibAssur();
}
