package com.arabsoft.gestionconvention.Entities;


import com.arabsoft.gestionconvention.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestionconvention.Entities.Cle.CleDemandeConv;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Entity
@Table(name = "demande_conv")
@IdClass(CleDemandeConv.class)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeConv {


    private String cin;
    private String cod_affect;
    @Id
    private String cod_conv;
    private String cod_lieu_geog;
    @Id
    private String cod_soc;
    private String cod_typ_depart;
    private String cod_user;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_dem_conv;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_min_fin;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_saisie;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_susp;

    private String etat_dem;
    @Id
    private String mat_pers;

    private String obs;
}