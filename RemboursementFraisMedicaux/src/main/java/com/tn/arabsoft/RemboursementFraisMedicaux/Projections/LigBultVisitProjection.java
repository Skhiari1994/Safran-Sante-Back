package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface LigBultVisitProjection{
         String getCod_soc();
        String getMat_pers();
        String getNum_fam();
        LocalDate getDat_soin();
        String getAbrv_act();
        String getCod_visit();
        Long getNum_lig();
        BigDecimal getMnt_honor();
        BigDecimal getMnt_remb();
        BigDecimal getMnt_net();
        Long getIndice();
        LocalDate getDat_act();
        String getPrf_typ();
        String getPrf_cod();
        BigDecimal getPrix_visit();
        BigDecimal getTaux_remb();
        BigDecimal getMut_mnt_net();
        String getLib_visit();
        String getLib_etablis();
}
