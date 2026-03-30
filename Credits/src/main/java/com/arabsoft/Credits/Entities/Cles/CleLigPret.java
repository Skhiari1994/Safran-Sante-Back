package com.arabsoft.Credits.Entities.Cles;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class CleLigPret implements Serializable {
    private String  cod_soc	;
    private String  mat_pers;
    private BigDecimal cod_pret;
    private BigDecimal  l_pret;
}
