package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;

import java.time.LocalDate;

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
