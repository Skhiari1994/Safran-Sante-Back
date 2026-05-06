package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;

import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface BultSoinProjection {

    String getCod_soc();

    String getMat_pers();

    String getNum_fam();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_soin();

    String getCod_bord();

    String getCod_assur();

    String getNum_soin();

    String getOrd_bult();

    BigDecimal getTot_honor();

    BigDecimal getTot_net();

    BigDecimal getTot_remb();

    String getReg_remb();

    String getCod_malad();

    String getNum_pec();

    String getCod_fil();

    String getNum_assur();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_prev_accouch();

    String getNat_bult();

    String getMat_pers_conj();

    String getNum_ass_conj();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_saisie();

    String getObs();

    String getObs_a();

    String getEnvoi();

    String getAnn_plaf_imp();

    String getReg_adh();

    String getMod_pay();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_vir();

    String getMat_int();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_nais();

    String getChoix();

    String getNum_retr();

    String getNom_complet_pers();

    String getLib_assur();

    String getLib_fill();

    String getLib_remb();

    String getNom_adherent();

}