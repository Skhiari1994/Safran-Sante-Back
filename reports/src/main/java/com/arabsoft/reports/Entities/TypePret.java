package com.arabsoft.reports.entities;

import com.arabsoft.reports.entities.cle.CleTypePret;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.ToString;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "type_pret")
@ToString
@IdClass(CleTypePret.class)
@SuppressWarnings({ "java:S116" })
public class TypePret {

    @Id
    private String cod_soc;

    @Id
    private String cod_grp_pret;

    @Id
    private String typ_pret;

    private String abrv_fixe;

    private String lib_pret;

    private String lib_pret_a;

    private Long taux_int;

    private Long duree_remb;

    private Long delai_grace;

    private Long nbr_tranche;

    private Long anciennete;

    private String typ_fond;

    private String typ_plafond;

    private Long indice;

    private Long taux_coop;

    private Long nbr_cariere;

    private Long delai_apres_ret;

    private String typ_ass_pret;

    private String int_fixe;

    private String abreviation;

    private String nat_typ;

    private String ass_pret;

    private String renouv;

    private String abrv_compta;

    private String libre_serv;

    private Long chap_comp;

    private String cod_op;

    private String typ_int;

    private Long plafond;

    private Long pourcent;

    private Long dure_renouv;

    private String commission;

    private String typ_taux;

    private String cpt_int;

    private String cpt_cour_terme;

}
