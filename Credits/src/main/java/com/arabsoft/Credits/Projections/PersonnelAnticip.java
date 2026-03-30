package com.arabsoft.Credits.Projections;

import com.arabsoft.Credits.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;

public interface PersonnelAnticip {

    String getMat_pers();
    String getNom();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_nais();
}
