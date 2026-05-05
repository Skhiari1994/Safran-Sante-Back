package com.arabsoft.gestion_adherent.projections;

import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface AffilPersProjection {

    String getMat_pers();

    String getCin();

    String getNom_pers();

    String getPren_pers();

    String getEtat_act();

    LocalDate getDat_emb();

    String getCod_sit();

    Long getNbr_enf();

    String getNum_assur();

    String getCorps();

    String getCod_affect();

    String getCod_typ_depart();

}
