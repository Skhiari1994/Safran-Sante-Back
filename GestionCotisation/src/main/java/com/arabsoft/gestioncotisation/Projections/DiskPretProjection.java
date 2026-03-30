package com.arabsoft.gestioncotisation.Projections;

import com.arabsoft.gestioncotisation.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DiskPretProjection {
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_disk();
    String getPret_disk();
    String getNum_retr();
    String getMat_pers();
    String  getNom_pers();
    BigDecimal getMontant();
    String getObservation();
    String getValid();
    String getCod_grp_pret();
    String  getTyp_pret();
    Long  getCod_pret();
}
