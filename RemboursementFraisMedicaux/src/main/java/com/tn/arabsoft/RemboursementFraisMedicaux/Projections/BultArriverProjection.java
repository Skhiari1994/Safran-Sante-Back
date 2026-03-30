package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BultArriverProjection {
    LocalDate getDat_saisie();

    String getCod_soc();

    String getMat_pers();

    String getNom_prenom();

    String getCod_fil();

    String getNum_fam();

    String getNom();

    String getStatut_famille();

    LocalDate getDat_soin();

    String getLib_remb();

    BigDecimal getTot_remb();

    BigDecimal getTot_mut();

    BigDecimal getTot_net();

    String getReg_adh();

    String getSolde();

    String getReclamation();

    String getMod_pay();

    LocalDate getDat_vir();

    String getNum_retr();

    String getCod_assur();

    String getCod_bord();

    String getReg_remb();
}
