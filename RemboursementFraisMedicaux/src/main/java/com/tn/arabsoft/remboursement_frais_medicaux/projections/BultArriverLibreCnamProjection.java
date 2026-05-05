package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;

import java.time.LocalDate;

public interface BultArriverLibreCnamProjection {

    String getAnn_plaf_imp();

    String getCod_assur();

    String getCod_bord();

    String getCod_fil();

    String getCod_malad();

    String getCod_soc();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_prev_accouch();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_saisie();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_soin();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_vir();

    String getDecis_med();

    String getEnvoi();

    String getMat_pers();

    String getNom_prenom();

    String getNum_retr();

    String getNom_pren();

    String getMat_pers_conj();

    String getMod_pay();

    String getNat_bult();

    String getNum_assur();

    String getNum_ass_conj();

    Integer getNum_fam();

    String getNum_pec();

    String getNum_soin();

    String getNum_soin_cnam();

    String getObs();

    String getObs_a();

    Integer getOrd_bult();

    String getReclam();

    String getReg_adh();

    String getReg_remb();

    Double getTot_honor();

    Double getTot_net();

    Double getTot_remb();

    Double getTot_remb_med();

    String getTyp_bult();
}
