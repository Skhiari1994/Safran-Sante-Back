package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.BultArriverCle;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "bult_arriver")
@IdClass(BultArriverCle.class)
public class BultArriver {

    @Id
    private String cod_soc;
    @Id
    private String mat_pers;
    @Id
    private Integer num_fam;
    @Id
    private LocalDate dat_soin;
    private String cod_bord;
    private String cod_assur;
    private String num_soin;
    private BigDecimal ord_bult;
    private BigDecimal tot_honor;
    private BigDecimal tot_net;
    private BigDecimal tot_remb;
    private String reg_remb;
    private String cod_malad;
    private String num_pec;
    private String cod_fil;
    private String num_assur;
    private LocalDate dat_prev_accouch;
    private String nat_bult;
    private String mat_pers_conj;
    private String num_ass_conj;
    private LocalDate dat_saisie;
    private String obs;
    private String obs_a;
    private String envoi;
    private String reclam;
    private String typ_bult;
    private String ann_plaf_imp;
    private BigDecimal tot_remb_med;
    private String num_soin_cnam;
    private String decis_med;
    private String reg_adh;
    private String mod_pay;
    private LocalDate dat_vir;

}
