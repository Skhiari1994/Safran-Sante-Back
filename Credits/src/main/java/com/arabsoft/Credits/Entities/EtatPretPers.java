package com.arabsoft.Credits.Entities;

import com.arabsoft.Credits.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.Credits.Entities.Cles.CleEtatPret;
import com.arabsoft.Credits.Entities.Cles.CleEtatPretPers;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@IdClass(CleEtatPretPers.class)
public class EtatPretPers {

    @Id
    private String cod_soc	;
    @Id
    private String  mat_pers;
    @Id
    private Long  cod_pret	;
    @Id
    private Long  num_etat_pret	;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_effet;
    private String  nat_etat_pret;
    private String type_anticip;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_debut_etat;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate  dat_fin_etat	;
    private BigDecimal mnt_anticipe;
    private BigDecimal  mnt_capital;
    private BigDecimal  mnt_interet;
    private BigDecimal  int_grace;
    private String  mode_payement	;
    private String  num_piece;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate  dat_piece;
    private String   cod_banq;
    private String   cod_agc;
    private String   nom_emet_piece	;
    private String   obs_etat;
    private String  cod_etat_pret;
    private String typ_etat;
    private String inject;
    private Long num_mvt;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_mvt;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_saisie;
    private String cod_user;
    private String  cod_grp_pret;
    private Long  num_vir;

}
