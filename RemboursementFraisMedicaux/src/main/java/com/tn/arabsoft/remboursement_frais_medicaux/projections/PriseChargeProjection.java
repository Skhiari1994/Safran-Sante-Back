package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PriseChargeProjection {

    String getCod_soc();

    String getMat_pers();

    String getNum_pec();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_pec();

    Integer getNum_fam();

    String getEtat_pec();

    String getPrf_typ();

    String getPrf_cod();

    BigDecimal getMnt_pec();

    BigDecimal getMnt_remb();

    LocalDate getDat_eff();

    String getNom();

    String getNomEtab();
}
