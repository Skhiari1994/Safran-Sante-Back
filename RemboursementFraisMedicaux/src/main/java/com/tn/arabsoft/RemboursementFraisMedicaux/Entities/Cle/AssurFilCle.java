package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AssurFilCle implements Serializable {
    private String COD_ASSUR;
    private String COD_FIL;
}
