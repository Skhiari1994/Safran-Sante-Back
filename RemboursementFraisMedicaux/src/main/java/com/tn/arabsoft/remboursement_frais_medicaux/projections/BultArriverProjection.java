package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
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
