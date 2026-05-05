package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.remboursement_frais_medicaux.configuration.CustomLocalDateDeserializer;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigBultAct;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
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
@Table(name = "lig_bult_act")
@IdClass(CleLigBultAct.class)
@SuppressWarnings({ "java:S116" })
public class LigBultAct {

  @Id
  private String cod_soc;

  @Id
  private String mat_pers;

  @Id
  private Long num_fam;

  @Id
  @JsonDeserialize(using = CustomLocalDateDeserializer.class)
  private LocalDate dat_soin;

  @Id
  private String abrv_act;

  @Id
  @JsonDeserialize(using = CustomLocalDateDeserializer.class)
  private LocalDate dat_act;

  @Id
  private String cod_act;

  @Id
  private Long num_lig;

  private String let_cod;

  private Long cot_act;

  private BigDecimal act_prix;

  private BigDecimal mnt_honor;

  private BigDecimal mnt_remb;

  private String accord_act;

  private BigDecimal mnt_net;

  private Long indice;

  private String prf_typ;

  private String prf_cod;

  private Long mut_mnt_net;

  private String num_pec_act;

  private BigDecimal taux_act;

  private String typ_prf;

}
