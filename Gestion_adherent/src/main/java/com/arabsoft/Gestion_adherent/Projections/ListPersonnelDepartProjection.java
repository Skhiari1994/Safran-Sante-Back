package com.arabsoft.gestion_adherent.projections;

import com.arabsoft.gestion_adherent.configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface ListPersonnelDepartProjection {
   String getMat_pers();

   String getNomPren();

   String getCod_affect();

   String getLibAffect();

   String getCod_lieu_geog();

   String getLib_lieu();

   String getCorps();

   LocalDate getDat_nais();

   LocalDate getDat_emb();

   String getCod_typ_depart();

   String getLibDepart();

   @JsonDeserialize(using = CustomLocalDateDeserializer.class)
   LocalDate getDat_depart();

}
