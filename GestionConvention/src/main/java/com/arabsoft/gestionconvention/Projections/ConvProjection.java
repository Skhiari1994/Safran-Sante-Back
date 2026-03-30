package com.arabsoft.gestionconvention.Projections;


import com.arabsoft.gestionconvention.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;

public interface ConvProjection {

    String getCod_conv();
    String getCod_soc();
    String getMat_pers();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_dem_conv();

    String getEtat_dem();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_saisie();

    String getCod_user();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_min_fin();

    String getObs();
    String getCod_lieu_geog();
    String getCod_typ_depart();
    String getCod_affect();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_susp();

    String getCin();
    String getLib_conv(); // From subquery: select lib_conv from convention
    String getNom();      // From subquery: select nom_pers || ' ' || pren_pers from personnel
}
