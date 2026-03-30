package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CleRefEtablis implements Serializable {
    private String prf_typ;
    private String prf_cod	;
}
