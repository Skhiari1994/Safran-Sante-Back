package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigMedArriverCle;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "lig_med_arriver")
@IdClass(LigMedArriverCle.class)
@SuppressWarnings({ "java:S116" })
public class LigMedArriver {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private Integer num_fam;

    @Id
    private LocalDate dat_soin;

    private String abrv_act;

    private String cod_med;

    @Id
    @Column(insertable = true, updatable = false)
    private Integer num_lig;

    private Integer indice;

    private Double mnt_honor;

    private Double mnt_net;

    private Double mnt_remb;

    private String accord_med;

    private LocalDate dat_act;

    private String prf_typ;

    private String prf_cod;

    private String num_pec_med;

    private Double mdc_prix;

    private Double med_prix;

    private Double prix_remb;

    private Integer num_lig_med;

    private Double mut_mnt_net;

    private Integer nbr_j;

    private String decis_med;

}
