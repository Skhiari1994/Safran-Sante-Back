package com.arabsoft.Credits.Entities;

import com.arabsoft.Credits.Entities.Cles.CleDetRetenueMens;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@IdClass(CleDetRetenueMens.class)
public class DetRetenueMens {

    @Id
    private String cod_soc;
    @Id
    private LocalDate mois_retenue;
    @Id
    private String  mat_pers;
    @Id
    private String  abrv_fixe;
    @Id
    private Long  cod_pret;
    @Id
    private Long  l_pret;
    private BigDecimal mnt_period	;
    private BigDecimal  mnt_int;

}
