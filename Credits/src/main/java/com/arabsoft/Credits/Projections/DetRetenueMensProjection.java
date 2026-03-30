package com.arabsoft.Credits.Projections;

import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DetRetenueMensProjection {

     String getCod_soc();

     LocalDate getMois_retenue();

     String  getMat_pers();
     String  getAbrv_fixe();
     Long getCod_pret();
     Long  getL_pret();
     BigDecimal getMnt_period()	;
     BigDecimal  getMnt_int();
     String getCod_grp_pret();
     String getTyp_pret();
     String getLib_pret();
}
