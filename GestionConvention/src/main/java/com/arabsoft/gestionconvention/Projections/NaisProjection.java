package com.arabsoft.gestionconvention.Projections;

import com.arabsoft.gestionconvention.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;

public interface NaisProjection {
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_nais();
    String getCorps();

}
