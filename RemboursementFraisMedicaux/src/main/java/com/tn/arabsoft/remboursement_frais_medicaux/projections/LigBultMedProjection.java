package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface LigBultMedProjection {

    String getCod_soc();

    String getMat_pers();

    String getNum_fam();

    LocalDate getDat_soin();

    String getAbrv_act();

    String getCod_med();

    Long getNum_lig();

    Long getIndice();

    BigDecimal getMnt_honor();

    BigDecimal getMnt_net();

    BigDecimal getMnt_remb();

    String getAccord_med();

    LocalDate getDat_act();

    String getPrf_typ();

    String getPrf_cod();

    String getNum_pec_med();

    BigDecimal getMdc_prix();

    BigDecimal getMed_prix();

    BigDecimal getPrix_remb();

    Long getNum_lig_med();

    BigDecimal getMut_mnt_net();

    Long getNbr_j();

    String getLib_med();

    String getLib_etablis();

}
