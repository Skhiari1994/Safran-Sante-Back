package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.ClePlafondAssur;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.ClePlafondCnam;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@IdClass(ClePlafondCnam.class)
public class PlafondCnam {

    @Id
    private Long  annee_cnam;
    @Id
    private String  cod_soc	;
    @Id
    private String  mat_pers;
    private BigDecimal plafond;
    private BigDecimal  sold_plaf;
    private BigDecimal sold_cnam_estim;

}
