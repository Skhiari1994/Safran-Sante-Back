package com.arabsoft.gestionindemnites.Projections;

import com.arabsoft.gestionindemnites.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DemandeDonProjection {

    String getTyp_don();

    String getCod_soc();

    String getMat_pers();
    String getNom_pers();

    Long getNum_fam();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_dem_don();
    String getNat_don();
    String getLib_nat_don();
    BigDecimal getMnt_dem_don();
    String getRaison();
    String getEtat_dem();
    BigDecimal getMnt_acc_don();
    String getEtat_act();
    String getCod_lieu_geog();
    String getCorps();
    String getCod_typ_depart();
    String getCod_affect();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_debut();
    String getCod_dest();
    String getCod_fond();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_deblocage();
    String getMode_payement();
    Long getRef_payement();
    String getNum_piece();
    String getRib();
    String getCod_user();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_saisie();
    String getTyp_benificiaire();
    Long getMnt_livre();
    Long getSeq_ecrt();
    String getImput_cpt();
    String getRef_metier();
    Long getNum_vir();

}
