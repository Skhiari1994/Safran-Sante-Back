package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface LigBultProjection {
     String getCod_soc();
    String getMat_pers();
    String getNum_fam();
    LocalDate getDat_soin();
    String getAbrv_act();
    Long getNum_lig();
    String getPrf_typ();
    String getPrf_cod();
    LocalDate getDat_act();
    Long getIndice();
    BigDecimal getMnt_honor();
    BigDecimal getMnt_net();
    BigDecimal getMnt_remb();
    String getObs();
    String getObs_a();
    Long getNbr_piece();
    Long getNbr_vign();
    String getNat_act();
    Long getMtt_acte();
    BigDecimal getTaux_act();
    String getPlafonne();
    BigDecimal getPlafond();
    String getA_indice();
    String getCtr_duree();
    Long getDuree_act();
    String getImput_plaf();
    String getLib_act();
    String getLib_etablis();
}
