package com.arabsoft.Gestion_adherent.Entities;

import com.arabsoft.Gestion_adherent.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.Gestion_adherent.Entities.Cle.CleDepartPers;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(CleDepartPers.class)
public class Depart_Pers {
    @Id
    private String cod_soc;
    @Id
    private String  mat_pers;
    private String  cod_typ_depart;
    @Id
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_depart;
    private LocalDate  dat_sais_depart;
    private String obs_depart;
    private String etat_depart;
    private String  corps;
    private String  cod_affect	;
    private String  cod_lieu_geog	;

}
