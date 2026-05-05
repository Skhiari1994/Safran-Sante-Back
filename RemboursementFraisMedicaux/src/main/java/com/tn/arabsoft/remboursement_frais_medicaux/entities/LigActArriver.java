package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigActArriverCle;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "lig_act_arriver")
@IdClass(LigActArriverCle.class)
@SuppressWarnings({ "java:S116" })
public class LigActArriver {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private Integer num_fam;

    @Id
    private LocalDate dat_soin;

    private Integer indice;

    private String abrv_act;

    private String cod_act;

    @Id
    private Integer num_lig;

    private String let_cod;

    private Double cot_act;

    private Double act_prix;

    private Double mnt_honor;

    private Double mnt_remb;

    private String accord_act;

    private Double mnt_net;

    private LocalDate dat_act;

    private String prf_typ;

    private String prf_cod;

    private Double mut_mnt_net;

    private String num_pec_act;

    private String decis_act;

}
