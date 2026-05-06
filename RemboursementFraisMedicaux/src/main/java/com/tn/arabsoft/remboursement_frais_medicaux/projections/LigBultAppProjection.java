package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface LigBultAppProjection {

    String getCod_soc();

    String getMat_pers();

    Long getNum_fam();

    LocalDate getDat_soin();

    String getAbrv_act();

    String getCod_app();

    Long getNum_lig();

    BigDecimal getMnt_honor();

    BigDecimal getMnt_net();

    BigDecimal getMnt_remb();

    String getAccord_app();

    Long getIndice();

    LocalDate getDat_act();

    String getPrf_typ();

    String getPrf_cod();

    String getNum_pec_app();

    String getNum_lig_app();

    BigDecimal getMut_mnt_net();

    String getLib_app();

    String getLib_etablis();

}
