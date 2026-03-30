package com.arabsoft.gestionindemnites.DTO;

import com.arabsoft.gestionindemnites.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestionindemnites.Entities.DemandeDons;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
public class DeblocageDemDonDTO {

    private String typ_don;
    private String cod_soc;
    private String mat_pers;
    private Long num_fam;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_dem_don;
    private DemandeDons demandeDon; // Object containing updated values


}
