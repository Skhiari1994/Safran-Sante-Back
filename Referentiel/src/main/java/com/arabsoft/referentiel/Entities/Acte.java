package com.arabsoft.referentiel.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "acte")
@ToString
@SuppressWarnings({ "java:S116" })
public class Acte {

    @Column(name = "type_act")
    @JsonProperty("type_act") // Garantit que le format JSON en entrée/sortie reste inchangé pour le frontend
    private String typeAct;

    @Id
    @Column(name = "abrv_act")
    @JsonProperty("abrv_act") // Garantit que le format JSON en entrée/sortie reste inchangé pour le frontend
    private String abrvAct;

    @Column(name = "lib_act")
    @JsonProperty("lib_act") // Garantit que le format JSON en entrée/sortie reste inchangé pour le frontend
    private String libAct;

    @Column(name = "lib_act_a")
    @JsonProperty("lib_act_a") // Garantit que le format JSON en entrée/sortie reste inchangé pour le frontend
    private String libActA;

    private String a_indice;

    private Long mtt_acte;

    private LocalDate dat_acte;

    private Long taux_act;

    private String plafonne;

    private Long plafond;

    private String verif_piece;

    private String nat_act;

    private String verif_vign;

    private Long duree_act;

    private String plafon_prest;

    private String ctr_duree;

    private String sexe;

    private String parente;

    private String imput_plaf;

}
