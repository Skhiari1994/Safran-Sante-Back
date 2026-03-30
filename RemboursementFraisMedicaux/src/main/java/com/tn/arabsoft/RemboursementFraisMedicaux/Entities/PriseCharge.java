package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.ClePriseCharge;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Data
@IdClass(ClePriseCharge.class)
public class PriseCharge {

    @Id
    private String  cod_soc;
    @Id
    private String   mat_pers;
    @Id
    private String  num_pec;
    private LocalDate dat_pec;
    private Long  num_fam;
    private String  etat_pec;
    private String  prf_typ;
    private String  prf_cod	;
    private BigDecimal mnt_pec;
    private BigDecimal   mnt_remb;
    private LocalDate  dat_eff;

}
