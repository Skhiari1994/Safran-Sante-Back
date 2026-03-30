package com.arabsoft.Credits.Projections;

import java.time.LocalDate;

public interface PersonnelPrejection {


    String getNumRetr();
    String getMatPers();
    String getNom();
    LocalDate getDatEmb();
    LocalDate getDatRetraite();
    String getCodSit();
    Integer getNbrEnf();
    LocalDate getDateNaissance();
    String getCodLieuGeog();
    String getLibLieu();
    String getCodTypDepart();
    String getLibTypDepart();
    String getCodAffect();
    String getLibAffect();
    String getCorps();
    String getPersCarte();

}
