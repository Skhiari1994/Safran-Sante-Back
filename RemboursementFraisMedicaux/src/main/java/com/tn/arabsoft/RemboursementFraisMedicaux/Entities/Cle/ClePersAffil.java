package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ClePersAffil implements Serializable {
    private String cod_soc;
    private String   mat_pers;
    private Long   annee_aff;
}
