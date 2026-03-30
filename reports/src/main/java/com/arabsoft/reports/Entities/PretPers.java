package com.arabsoft.reports.Entities;

 import com.arabsoft.reports.Entities.Cle.ClePretPers;
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
@IdClass(ClePretPers.class)
public class PretPers {
    @Id
   private String cod_soc;
    @Id
    private String  mat_pers;
    @Id
    private Long  cod_pret;
    private Long  num_dem_pret;
    private LocalDate prt_dat_dem;
    private BigDecimal  prt_mnt_dem;
    private String  cod_grp_pret;
    private String  typ_pret;
    private String cod_etat_pret;
    private String typ_etat;
    private String org_pret;
    private String mod_remb;
    private String methode_calc;
    private String  cod_dept_pers;
    private String cod_serv_pers;
    private String  cod_motif_pers;
    private String cod_categ_pers;
    private String  cod_cat_pers;
    private String   cod_grad_pers;
    private String  adm_tech;
    private String  cod_affect;
    private String cod_lieu_geog;
    private String  corps;
    private String   cod_typ_depart;
    private Long   nbr_enf;
    private LocalDate  prt_dat_acc;
    private LocalDate prt_dat_deb;
    private LocalDate prt_dat_fin;
    private BigDecimal   prt_ech;
    private BigDecimal prt_taux;
    private BigDecimal  prt_mnt_glb;
    private BigDecimal  prt_mnt_prm;
    private BigDecimal  delai_grace;
    private BigDecimal  prt_int_grace;
    private BigDecimal prt_mnt_rem;
    private BigDecimal   rem_men	;
    private BigDecimal  dern_rem_men;
    private BigDecimal   prt_rendu;
    private BigDecimal   prt_rendu_int;
    private Long   nbr_retenue;
    private LocalDate  dat_deblocage;
    private LocalDate  dat_comptable;
    private String mode_reglement;
    private String piece_reglement;
    private BigDecimal mnt_report;
    private Long cod_pret_ant;
    private String ass_pret;
    private String objet_pret;
    private LocalDate dat_saisie;
    private String cod_user;
    private String cod_motif_susp;
    private Long cod_pret_ref;
    private Long  num_etat_pret;
    private String  amort_pret;
    private Long  nbr_tranche;
    private LocalDate dat_effet;
    private BigDecimal prt_mnt_debloque;
    private BigDecimal prt_int_tranch;
    private Long  num_lig_dem_pret;
    private BigDecimal  prt_interet;
    private Long num_comm;
    private Long seq_ecrt;
    private Long  num_vir;

}
