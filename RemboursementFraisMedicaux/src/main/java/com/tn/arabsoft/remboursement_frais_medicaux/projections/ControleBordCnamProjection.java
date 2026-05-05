package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ControleBordCnamProjection {

    String getMat_pers();

    String getNom_complet_pers();

    String getCod_fil();

    String getNum_fam();

    String getNom_pers();

    LocalDate getDat_saisie();

    LocalDate getDat_soin();

    String getSolde();

    BigDecimal getTot_honor();

    BigDecimal getTot_net();

    String getReg_adh();

    String getMod_pay();

    LocalDate getDat_vir();

    BigDecimal getTot_remb();
}
