package com.arabsoft.referentiel.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Acte {
    private String type_act;
    @Id
    private String  abrv_act;
    private String  lib_act;
    private String    a_indice;
    private Long    mtt_acte;
    private LocalDate dat_acte;
    private Long    taux_act;
    private String    plafonne;
    private Long   plafond;
    private String   verif_piece;
    private String   nat_act;
    private String   verif_vign;
    private String   lib_act_a;
    private Long    duree_act;
    private String    plafon_prest;
    private String    ctr_duree;
    private String  sexe;
    private String  parente	;
    private String   imput_plaf;

}
