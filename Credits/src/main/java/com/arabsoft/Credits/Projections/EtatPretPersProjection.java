package com.arabsoft.Credits.Projections;

import com.arabsoft.Credits.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface EtatPretPersProjection {

    String getCod_soc();
    String getMat_pers();
    Long getCod_pret();
    Long getNum_etat_pret();
    LocalDate getDat_effet();
    String getNat_etat_pret();
    String getType_anticip();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_debut_etat();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_fin_etat();
    BigDecimal getMnt_anticipe();
    BigDecimal getMnt_capital();
    BigDecimal getMnt_interet();
    BigDecimal getInt_grace();
    String getMode_payement();
    String getNum_piece();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_piece();
    String getCod_banq();
    String getCod_agc();
    String getNom_emet_piece();
    String getObs_etat();
    String getCod_etat_pret();
    String getTyp_etat();
    String getInject();
    Long getNum_mvt();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_mvt();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_saisie();
    String getCod_user();
    String getCod_grp_pret();
    Long getNum_vir();
    String getNom();
}
