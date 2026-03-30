package com.arabsoft.gestionindemnites.Entities;

import com.arabsoft.gestionindemnites.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestionindemnites.Entities.Cle.CleDemandeDons;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="DEMANDE_DONS")
@IdClass(CleDemandeDons.class)
public class DemandeDons {
        @Id
        public String typ_don;
        @Id
        public String cod_soc;
        @Id
        public String mat_pers;
        @Id
        public Long num_fam;
        @Id
        @JsonDeserialize(using = CustomLocalDateDeserializer.class)
        public LocalDate dat_dem_don;
        public String nat_don;
        public BigDecimal mnt_dem_don;
        public String raison;
        public String etat_dem;
        public BigDecimal mnt_acc_don;
        public String etat_act;
        public String cod_lieu_geog;
        public String corps;
        public String cod_typ_depart;
        public String cod_affect;
        @JsonDeserialize(using = CustomLocalDateDeserializer.class)
        public LocalDate dat_debut;
        public String cod_dest;
        public String cod_fond;
        @JsonDeserialize(using = CustomLocalDateDeserializer.class)
        public LocalDate dat_deblocage;
        public String mode_payement;
        public Long ref_payement;
        public String num_piece;
        public String rib;
        public String cod_user;
        @JsonDeserialize(using = CustomLocalDateDeserializer.class)
        public LocalDate dat_saisie;
        public String typ_benificiaire;
        public Long mnt_livre;
        public Long seq_ecrt;
        public String imput_cpt;
        public String ref_metier;
        public Long num_vir;

}
