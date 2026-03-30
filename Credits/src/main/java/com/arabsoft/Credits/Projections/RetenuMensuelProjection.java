package com.arabsoft.Credits.Projections;

import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RetenuMensuelProjection {

     String getCod_soc();
     LocalDate getMois_retenue();
     String  getMat_pers();
     String  getAbrv_fixe();
     BigDecimal getMnt_retenue();
     String   getValid();
     LocalDate   getDate_fin();
     String   getMotif();
     String getNom();
}
