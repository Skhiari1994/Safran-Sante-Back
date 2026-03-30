package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

public interface BordArriverProjection {
    String getCod_bord();

    LocalDate getDat_bord();

    String getNbr_bult();

    String getTot_remb();

    String getValid();

    String getLib_assurance();

    String getTot();

    String getCod_assur();
    String getValid_Bord();
    String getReg_bord();

}
