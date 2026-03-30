package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.ClePlafondAssur;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@IdClass(ClePlafondAssur.class)
public class PlafondAssur {
    @Id
    private Long annee_assur;
    @Id
    private String  cod_soc;
    @Id
    private String   mat_pers;
    private String   cod_assur;
    private BigDecimal plafond;
    private BigDecimal sold_plaf;
    private BigDecimal sold_assur_estim;

}
