package com.arabsoft.gestionindemnites.Entities.Cle;

import com.arabsoft.gestionindemnites.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
@Getter
@Setter
public class ClePieceDemDons implements Serializable{
    private static final Long serialVersionUID = 1L;
    private String typ_don;
    private String cod_soc;
    private String mat_pers;
    private Long num_fam;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_dem_don;
    private String cod_piece;

}
