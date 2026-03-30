package com.arabsoft.gestionindemnites.DTO;

import com.arabsoft.gestionindemnites.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestionindemnites.Entities.DemandeDons;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class UpdateDemandeClotureDTO {

    private String typ_don;
    private String cod_soc;
    private String mat_pers;
    private BigDecimal mnt_acc_don;
    private BigDecimal mnt_dem_don;
    private Long num_fam;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_dem_don;
    private DemandeDons demandeDon; // Object containing updated values


}