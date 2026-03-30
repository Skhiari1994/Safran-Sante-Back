package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.RemboursementFraisMedicaux.Configuration.CustomLocalDateDeserializer;

import java.time.LocalDate;

public interface BordereauProjection {

    String getCod_bord();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_bord();

}
