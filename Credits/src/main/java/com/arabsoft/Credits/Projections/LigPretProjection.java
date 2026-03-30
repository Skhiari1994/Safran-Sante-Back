package com.arabsoft.Credits.Projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface LigPretProjection {


    String getCodSoc();
    String getMatPers();
    String getNom();
    String getType();
    BigDecimal getCodPret();
    BigDecimal getLPret();
    String getCodTypBul();
    LocalDate getMoisPretPrevu();
    LocalDate getMoisPret();
    BigDecimal getMntPeriod();
    BigDecimal getMntInt();
    BigDecimal getIntGrace();
    BigDecimal getCapRest();
    String getValPret();
    String getRegPret();
    String getNatureEtatPret();
    BigDecimal getTauxInt();
    String getNumRetr();
}
