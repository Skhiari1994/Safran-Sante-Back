package com.arabsoft.gestion_adherent.projections;

import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface DiskPretProjection {

    LocalDate getDat_disk();

    String getPret_disk();

    String getNum_retr();

    String getMat_pers();

    BigDecimal getMontant();

    String getObservation();

    String getValid();

    String getCod_grp_pret();

    String getTyp_pret();

    Long getCod_pret();

    String getNom();
}
