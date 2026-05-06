package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface LigAppArriverProjection {

    LocalDate getDat_act();

    String getCod_app();

    String getLib_app();

    BigDecimal getMnt_honor();

    BigDecimal getMnt_remb();

    String getPrf_type();

    Integer getPrf_typ();

    String getLib_org();

    String getAccord_app();

    String getMat_pers();

    String getCod_soc();

    String getPrf_cod();

    String getNum_fam();

    LocalDate getDat_soin();

    String getAbrv_act();

    String getNum_pec_app();

}
