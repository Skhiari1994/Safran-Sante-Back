package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigVisitArriverCle;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "lig_visit_arriver")
@IdClass(LigVisitArriverCle.class)
public class LigVisitArriver {

    @Id
    private String cod_soc;
    @Id
    private String mat_pers;
    @Id
    private Long num_fam;
    @Id
    private LocalDate dat_soin;
    private Long indice;
    private String abrv_act;
    private String cod_visit;
    @Id
    private Long num_lig;
    private Double mnt_honor;
    private Double mnt_remb;
    private Double mnt_net;
    private LocalDate dat_act;
    private String prf_typ;
    private String prf_cod;
    private Double prix_visit;
    private Double taux_remb;
    private Double mut_mnt_net;
    private String decis_med;
}
