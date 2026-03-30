package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.RemboursementFraisMedicaux.Configuration.CustomLocalDateDeserializer;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class LigBultCle implements Serializable  {

    private String cod_soc;
    private String mat_pers;
    private String num_fam;
    private LocalDate dat_soin;
    private String abrv_act;
}
