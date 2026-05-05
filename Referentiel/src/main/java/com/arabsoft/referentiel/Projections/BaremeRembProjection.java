package com.arabsoft.referentiel.projections;

import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface BaremeRembProjection {

     String getCod_fil();

     String getAbrv_act();

     String getCod_assur();

     String getA_indice();

     String getMtt_acte();

     LocalDate getDat_acte();

     String getTaux_act();

     String getPlafonne();

     String getPlafond();

     String getVerif_piece();

     String getNat_act();

     String getVerif_vign();

     String getDuree_act();

     String getPlafon_prest();

     String getLib_act();

}
