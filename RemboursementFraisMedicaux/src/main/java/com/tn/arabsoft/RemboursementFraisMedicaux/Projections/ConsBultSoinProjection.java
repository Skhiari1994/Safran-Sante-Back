package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.RemboursementFraisMedicaux.Configuration.CustomLocalDateDeserializer;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ConsBultSoinProjection {
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
    String getChoix();
    String getNom_complet_pers(); // Concaténation nom + prénom
    String getLib_assur();
    String getLib_fill();
    String getNom_adherent();
    BigDecimal getMnt_tot_mut();
    BigDecimal getMnt_honor();
    BigDecimal getSold();
    BigDecimal getMnt_tot();


}
