package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.CleLigBultApp;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.CleLigBultArriver;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@IdClass(CleLigBultArriver.class)
public class LigBultArriver {
    @Id
   private String cod_soc;
    @Id
    private String  mat_pers;
    @Id
    private Long  num_fam;
    @Id
    private LocalDate dat_soin;
    private String abrv_act;
    private Long num_lig;
    private String prf_typ;
    private String prf_cod;
    private LocalDate  dat_act	;
    private Long   indice;
    private BigDecimal   mnt_honor;
    private BigDecimal  mnt_net;
    private BigDecimal  mnt_remb;
    private String  obs;
    private String obs_a;
    private Long   nbr_piece;
    private Long   nbr_vign;
    private String  nat_act	;
    private BigDecimal mtt_acte;
    private BigDecimal  taux_act;
    private String plafonne;
    private BigDecimal   plafond;
    private String  a_indice;
    private String  ctr_duree;
    private Long duree_act;
    private String  imput_plaf;

}
