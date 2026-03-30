package com.arabsoft.gestionconvention.Projections;

import com.arabsoft.gestionconvention.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;

public interface DemondeConvProjection {

    String getNom_pren();

    String getCorps();

    String getCod_affect();

    String getLib_affect();

    String getCod_lieu_geog();

    String getLib_lieu();

    String getCod_typ_depart();

    String getLib_depart();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)

    LocalDate getDat_depart();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)

    LocalDate getDat_dem_cov();

    String getObs();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)

    LocalDate getDat_saisie();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)

    LocalDate getDat_min_fin();

    String getEtat_dem();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_susp();

    String getCin();

    String getNum_retr();

    String getMat_pers();

    String getMat_int();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_nais();




    String getCodConv();
    String getCodSoc();
    String getMatPers();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDatDemConv();
    String getEtatDem();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDatSaisie();
    String getCodUser();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDatMinFin();

    String getCodLieuGeog();
    String getCodTypDepart();
    String getCodAffect();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDatSusp();


}
