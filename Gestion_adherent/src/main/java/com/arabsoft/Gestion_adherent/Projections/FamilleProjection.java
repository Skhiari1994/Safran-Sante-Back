package com.arabsoft.Gestion_adherent.Projections;

import com.arabsoft.Gestion_adherent.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Date;

public interface FamilleProjection {


      String getCod_soc();
      String getMat_pers();
      Long   getNum_fam();
      String getParente();
      String getNom_pren();
      @JsonDeserialize(using = CustomLocalDateDeserializer.class)
      LocalDate getDat_naiss();
      String getSexe();
      String getCod_sit();
      String getHandicap();
      String getCod_activite();
      @JsonDeserialize(using = CustomLocalDateDeserializer.class)
      LocalDate getDat_dece();
      String getPec();
      @JsonDeserialize(using = CustomLocalDateDeserializer.class)
      LocalDate getDat_pec();
      @JsonDeserialize(using = CustomLocalDateDeserializer.class)
      LocalDate getDat_mar();
      String getNom_jf();
      String getNum_ass_conj();
      String getMat_pers_conj();
      String getPec_mut();
      @JsonDeserialize(using = CustomLocalDateDeserializer.class)
      LocalDate getDat_pec_mut();
      String getLib_activite();
}
