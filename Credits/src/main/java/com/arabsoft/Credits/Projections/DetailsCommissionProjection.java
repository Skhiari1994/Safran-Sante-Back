package com.arabsoft.Credits.Projections;

import com.arabsoft.Credits.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Id;

import java.time.LocalDate;

public interface DetailsCommissionProjection {

    String getCodSoc();
    String getNumComm();
    String getMatPers();
    String getNumDemPret();
    String getResultatComm();
    Double getMntAcc();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDatEffet();
    Integer getNbrEchAcc();
    Integer getDelaiGrace();
    Integer getNbrTranche();
    String getCodRejet();
    String getCodEtatPret();
    String getTypEtat();
    String getObsComm();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDatDem();
    String getTypPret();
    String getCodGrpPret();
    Double getMntDem();
    String getLibPret();
    String getNomPren();
}
