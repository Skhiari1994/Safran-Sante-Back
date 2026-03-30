package com.arabsoft.gestionconvention.Projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface OffDemandeConvProjection {

    String getCod_conv();
    String getCod_soc();
    String getMat_pers();
    String getCod_off();
    LocalDate getDat_off_dem();
    LocalDate getDat_fin_off();
    BigDecimal getMnt_off();
    String getEtat_off_dem();
    String getCod_user();
    LocalDate getDat_saisie();
    Integer getSeq();
    LocalDate getDat_susp();
    String getNum_tel();
    String getCod_mot_susp();
    String getRenouv();
    LocalDate getDat_renouv();
    String getObs_off();
    String getLib_off();
    String getLib_mot();
}
