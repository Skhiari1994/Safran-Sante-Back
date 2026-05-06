package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface LigBultArriverProjection {

    String getCod_bord();

    String getMat_pers();

    Integer getNum_fam();

    LocalDate getDat_soin();

    String getAbrv_act();

    Integer getNum_lig();

    String getPrf_typ();

    String getPrf_cod();

    LocalDate getDat_act();

    Double getIndice();

    Double getMnt_honor();

    Double getMnt_net();

    Double getMnt_remb();

    String getObs();

    String getObs_a();

    Integer getNbr_piece();

    Integer getNbr_vign();

    String getNat_act();

    Double getMtt_acte();

    Double getTaux_act();

    Boolean getPlafonne();

    Double getPlafond();

    Double getA_indice();

    Integer getCtr_duree();

    Integer getDuree_act();

    Double getImput_plaf();

    String getLib_act();

}
