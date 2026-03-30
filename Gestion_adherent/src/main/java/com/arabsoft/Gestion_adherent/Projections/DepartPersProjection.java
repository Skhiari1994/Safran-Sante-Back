package com.arabsoft.Gestion_adherent.Projections;

import com.arabsoft.Gestion_adherent.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Id;

import java.time.LocalDate;

public interface DepartPersProjection {

      String getCod_soc();
      String  getMat_pers();
      String getNomPers();
      String  getCod_typ_depart();
      String getLibDepart();
      @JsonDeserialize(using = CustomLocalDateDeserializer.class)
      LocalDate getDat_depart();
      @JsonDeserialize(using = CustomLocalDateDeserializer.class)
      LocalDate  getDat_sais_depart();
      String getObs_depart();
      String getEtat_depart();
      String  getCorps();
      String getLibCorps();
      String  getCod_affect()	;
      String getLibAffect();
      String  getCod_lieu_geog();
      String getLibLieu();
      @JsonDeserialize(using = CustomLocalDateDeserializer.class)
      LocalDate getDatNais();
      @JsonDeserialize(using = CustomLocalDateDeserializer.class)
      LocalDate getDatEmb();
}
