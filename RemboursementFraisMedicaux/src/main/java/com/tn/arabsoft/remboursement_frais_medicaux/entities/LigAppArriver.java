package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigAppArriverCle;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "lig_app_arriver")
@IdClass(LigAppArriverCle.class)
@SuppressWarnings({ "java:S116" })
public class LigAppArriver {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private Integer num_fam;

    @Id
    private LocalDate dat_soin;

    private String abrv_act;

    private String cod_app;

    @Id
    private Integer num_lig;

    private Double mnt_honor;

    private Double mnt_net;

    private Double mnt_remb;

    private String accord_app;

    private Integer indice;

    private LocalDate dat_act;

    private String prf_typ;

    private String prf_cod;

    private String num_pec_app;

    private Integer num_lig_app;

    private Double mut_mnt_net;

}
