package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
public interface BordEnvoiProjection {

    String getCod_bord();

    LocalDate getDat_bord();

    String getNbr_bult();

    String getLib_assurance();

    LocalDate getDat_deb();

    LocalDate getDat_fin();

    String getTot_net();

    String getTot_honor();

    String getTot_remb();

    String getCod_assur();

    String getReg_bord();

}
