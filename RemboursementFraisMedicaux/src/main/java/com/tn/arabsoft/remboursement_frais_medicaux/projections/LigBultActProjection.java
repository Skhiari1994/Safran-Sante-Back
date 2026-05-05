package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface LigBultActProjection {
    String getCod_soc();

    String getMat_pers();

    Long getNum_fam();

    LocalDate getDat_soin();

    String getAbrv_act();

    String getCod_act();

    Long getNum_lig();

    String getLet_cod();

    Long getCot_act();

    BigDecimal getAct_prix();

    BigDecimal getMnt_honor();

    BigDecimal getMnt_remb();

    String getAccord_act();

    BigDecimal getMnt_net();

    Long getIndice();

    LocalDate getDat_act();

    String getPrf_typ();

    String getPrf_cod();

    Long getMut_mnt_net();

    String getNum_pec_act();

    BigDecimal getTaux_act();

    String getTyp_prf();

    String getLib_act();

    String getLib_etablis();
}
