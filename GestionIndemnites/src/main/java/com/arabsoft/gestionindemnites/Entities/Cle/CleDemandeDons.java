package com.arabsoft.gestionindemnites.Entities.Cle;

import com.arabsoft.gestionindemnites.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CleDemandeDons implements Serializable {
    private static final Long serialVersionUID = 1L;

    private String typ_don;
    private String cod_soc;

    private String mat_pers;

    private Long num_fam;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_dem_don;
}
