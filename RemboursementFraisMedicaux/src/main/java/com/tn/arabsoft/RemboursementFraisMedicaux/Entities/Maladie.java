package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class Maladie {
    @Id
   private String cod_malad;
    private String  lib_malad;
    private String  apci;
    private String  lib_malad_a;
    private BigDecimal mnt_seuil;

}
