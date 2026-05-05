package com.tn.arabsoft.remboursement_frais_medicaux.projections;

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
