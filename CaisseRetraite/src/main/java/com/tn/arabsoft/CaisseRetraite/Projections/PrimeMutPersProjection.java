package com.tn.arabsoft.CaisseRetraite.Projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PrimeMutPersProjection {

    String getCod_soc();

    String getMat_pers();

    Long getNum_prime();

    String getTyp_prime();

    LocalDate getDat_deb();

    LocalDate getDat_fin();

    BigDecimal getMnt_a_payer();

    BigDecimal getMnt_payer();

    String getMod_pay();

    String getRef_pay();

    LocalDate getDat_saisie();

    String getEtat_prime();

    String getCorps();

    String getCod_typ_depart();

    String getCod_affect();

    BigDecimal getMnt_param();

    String getNum_retr();

    Long getSeq_ecrt1();

    Long getSeq_ecrt2();

    String getImput();

    String getNom();

    String getMat_int();

    String getDat_nais();
}
