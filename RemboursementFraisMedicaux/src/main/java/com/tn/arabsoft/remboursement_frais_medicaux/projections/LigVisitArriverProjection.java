package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.time.LocalDate;

public interface LigVisitArriverProjection {

    LocalDate getDat_act();

    String getAbrv_act();

    String getLib_act();

    String getIndice();

    String getMnt_honor();

    String getMnt_net();

    String getMnt_remb();

    String getPrf_type();

    Integer getPrf_typ();

    String getLib_org();

    String getMat_pers();

    String getCod_soc();

    String getPrf_cod();

    String getNum_fam();

    LocalDate getDat_soin();

    String getCod_visit();

}
