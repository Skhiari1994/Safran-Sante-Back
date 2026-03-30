package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.RemboursementFraisMedicaux.Configuration.CustomLocalDateDeserializer;
import jakarta.persistence.Id;

import java.time.LocalDate;

public interface BordEnvoiPrejection {
    String getCod_bord();
    String getCod_soc();
    String getCod_assur();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_bord();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_deb();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_fin();
    Long getNbr_bult();
    String getTot_honor();
    String getTyp_bord();
    String getTot_net();
    String getValid_bord();
    String getReg_bord();
    String getTot_remb();
    String getEnvoi_bord();
    String getLib_assur();

}
