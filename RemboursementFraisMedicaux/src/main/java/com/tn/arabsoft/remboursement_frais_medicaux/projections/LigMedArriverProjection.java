package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface LigMedArriverProjection {

    LocalDate getDat_act();

    String getCod_med();

    String getLib_med();

    String getIndice();

    BigDecimal getMnt_honor();

    BigDecimal getMnt_remb();

    BigDecimal getMut_mnt_net();

    String getPrf_type();

    Integer getPrf_typ();

    String getLib_org();

    String getPrf_cod();

    String getAccord_med();

    String getNum_pec_med();

    String getMat_pers();

    String getCod_soc();

    String getNum_fam();

    LocalDate getDat_soin();

    String getAbrv_act();

    Integer getNbr_j();
}
