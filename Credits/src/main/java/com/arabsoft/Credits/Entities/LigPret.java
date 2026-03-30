package com.arabsoft.Credits.Entities;

import com.arabsoft.Credits.Entities.Cles.CleLigPret;
import com.arabsoft.Credits.Entities.Cles.ClePiecePRetPers;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(CleLigPret.class)
public class LigPret {
    @Id
    private String  cod_soc	;
    @Id
    private String  mat_pers;
    @Id
    private BigDecimal cod_pret;
    @Id
    private BigDecimal  l_pret;
    private String  cod_typ_bul	;
    private LocalDate mois_pret_prevu;
    private LocalDate  mois_pret;
    private BigDecimal  mnt_period;
    private BigDecimal  mnt_int;
    private BigDecimal  int_grace;
    private BigDecimal  cap_rest;
    private String  val_pret;
    private String reg_pret;
    private String nature_etat_pret;
    private BigDecimal taux_int;
    private String num_retr;

}
