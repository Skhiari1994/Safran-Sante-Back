package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface LigActArriverProjection {
    LocalDate getDat_act();

    String getCod_act();

    String getLib_act();

    String getIndice();

    BigDecimal getMnt_honor();

    BigDecimal getMut_mnt_net();

    BigDecimal getMnt_remb();

    String getPrf_type();

    String getLib_org();

    String getMat_pers();

    String getCod_soc();

    String getPrf_cod();

    String getNum_fam();

    LocalDate getDat_soin();

    String getAccord_act();

    String getNum_pec_act();

    Integer getType_etablis();

    String getLib_etablis();

    String getAbrv_act();

    String getNum_lig();
}