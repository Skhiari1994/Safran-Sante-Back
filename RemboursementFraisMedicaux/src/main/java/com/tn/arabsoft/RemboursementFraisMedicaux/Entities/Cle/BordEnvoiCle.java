package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BordEnvoiCle implements Serializable {
    private String cod_soc;
    private String cod_bord;
}
