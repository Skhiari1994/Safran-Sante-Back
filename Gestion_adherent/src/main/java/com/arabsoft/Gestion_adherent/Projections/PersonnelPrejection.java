package com.arabsoft.gestion_adherent.projections;

import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface PersonnelPrejection {

    String getCod_soc();

    String getMat_pers();

    String getCod_assur();

    String getNom_pers();

    String getSexe();

    String getCin();

    LocalDate getDat_nais();

    String getPren_pers();

    LocalDate getDat_emb();

    String getCod_sit();

    Long getNbr_enf();

    String getCod_retr();

    String getNum_retr();

    String getNum_assur();

    LocalDate getDat_ass();

    String getCod_pay();

    String getRib();

    String getNom_pers_a();

    String getPren_pers_a();

    String getCod_natp();

    String getCod_banq();

    String getCod_agc();

    LocalDate getDat_dece();

    String getEtat_act();

    LocalDate getDat_motif();

    String getCod_lieu_geog();

    Long getBas_plafond();

    String getNom_jf();

    String getNom_jf_a();

    Long getPhoto_pers();

    String getLieu_nais();

    Long getMnt_param();

    String getEtat_prof();

    LocalDate getDat_aff_cnam();

    String getCorps();

    String getCod_affect();

    LocalDate getDat_affect();

    String getCod_typ_depart();

    LocalDate getDat_depart();

    String getTyp_aff();

    String getCod_user();

    LocalDate getDat_maj();

    String getMat_int();

    String getPers_carte();

    String getFull_name();

    String getLib_lieu();

}
