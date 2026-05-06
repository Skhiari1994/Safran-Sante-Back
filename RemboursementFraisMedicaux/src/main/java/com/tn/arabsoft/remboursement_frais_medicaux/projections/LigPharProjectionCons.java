package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface LigPharProjectionCons {

    String getCod_soc();

    String getMat_pers();

    Long getNum_fam();

    LocalDate getDat_soin();

    String getCod_med();

    Long getNum_lig();

    Long getIndice();

    LocalDate getDat_act();

    String getPrf_typ();

    String getPrf_cod();

    BigDecimal getMnt_honor();

    BigDecimal getMnt_net();

    BigDecimal getMnt_remb();

    BigDecimal getMdc_prix();

    BigDecimal getMed_prix();

    BigDecimal getPrix_remb();

    String getObs();

    String getObs_a();

    Long getNbr_piece();

    Long getNbr_vign();

    String getAbrv_act();

    String getLib_med();

    String getLib_etablis();

}
