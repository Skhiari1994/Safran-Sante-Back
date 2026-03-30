package com.arabsoft.Gestion_adherent.Projections;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public interface AffilPersValid {

   String getCod_soc();
    String getMat_pers();
    String getNompren();
    LocalDate getDat_ass();
    String getNum_assur();
    String getTyp_aff();
    LocalDate getDat_dem();
    String getObs_aff();
    String getCoef_cot();
    String getEtat_aff();
    String getCorps();
    String getCod_typ_depart();
    String getCod_affect();

}
