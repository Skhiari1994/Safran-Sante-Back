package com.arabsoft.gestionindemnites.Entities;

import com.arabsoft.gestionindemnites.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestionindemnites.Entities.Cle.ClePieceDemDons;
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

import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="PIECE_DEM_DONS")
@IdClass(ClePieceDemDons.class)
public class PieceDemDons {

    @Id
    private String typ_don;
    @Id
    private String cod_soc;
    @Id
    private String mat_pers;
    @Id
    private Long num_fam;
    @Id
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_dem_don;
    @Id
    private String cod_piece;

    private Long nbr_copie;


}
