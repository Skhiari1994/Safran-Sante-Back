package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;

import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface BordereauProjection {

    String getCod_bord();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_bord();

}
