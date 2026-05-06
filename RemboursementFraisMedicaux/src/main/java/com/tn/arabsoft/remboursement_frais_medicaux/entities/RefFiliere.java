package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "ref_filliere")
@SuppressWarnings({ "java:S116" })
public class RefFiliere {

    @Id
    private String cod_fil;

    private String lib_fill;

    private String lib_fill_a;

    private Double mnt_adher;

    private Double mnt_enf;

    private Double mnt_conj;

    private Double mnt_pere;

    private Double mnt_mere;

    private String prorat_pec;

    private String lib_param;

    private String lib_param_a;

    private Double taux_param;

    private Integer nbr_membre;

    private String typ_plafond;

    private String bult_cnam;

    private String med_fam;

    private String cod_activ;

    private String bult_mut;

}
