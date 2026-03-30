package com.tn.arabsoft.CaisseRetraite.Projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PrimeRetraiteProjection {

    String getCod_soc();

    String getMat_pers();

    Long getNum_remb();

    String getNum_retr();

    String getCod_remb();

    LocalDate getDat_remb();

    String getBenef_prime();

    String getBenef_droi();

    BigDecimal getTaux_prime();

    LocalDate getDat_deb_ret();

    LocalDate getDat_fin_ret();

    String getEtat_prime();

    BigDecimal getMontant_cotis();

    BigDecimal getMontant_prime();

    BigDecimal getMontant_total();

    String getMod_pay();

    String getObs_retraite();

    String getImput_cpt();

    Long getSeq_ecrt();

    String getCheq_remb();

    String getNom();

    String getMat_int();

    String getDat_nais();
}
