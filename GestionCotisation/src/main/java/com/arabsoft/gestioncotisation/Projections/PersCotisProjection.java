package com.arabsoft.gestioncotisation.Projections;

import com.arabsoft.gestioncotisation.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;

public interface PersCotisProjection {


    String getCod_soc();
    String getMat_pers();
    String getCod_assur();
    String getNom_pers();
    String getSexe();
    String getCin();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_nais();
    String getPren_pers();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_emb();
    String getCod_sit();
    Float getNbr_enf();
    String getCod_retr();
    String getNum_retr();
    String getNum_assur();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_ass();
    String getCod_pay();
    String getRib();
    String getNom_pers_a();
    String getPren_pers_a();
    String getCod_natP();
    String getCod_banq();
    String getCod_agc();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_dece();
    String getEtat_act();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_motif();
    String getCod_lieu_geog();
    Float getBas_plafond();
    String getNom_jf();
    String getNom_jf_a();
    String getPhoto_pers();
    String getLieu_nais();
    Float getMnt_param();
    String getEtat_prof();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_aff_cnam();
    String getCorps();
    String getCod_affect();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_affect();
    String getCod_typ_depart();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_depart();
    String getTyp_aff();
    String getCod_user();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_maj();
    String getMat_int();
    String getPers_carte();



}
