package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CleDossierMld implements Serializable {
    private String cod_soc;
    private String  mat_pers;
    private String num_dos_mld;
}
