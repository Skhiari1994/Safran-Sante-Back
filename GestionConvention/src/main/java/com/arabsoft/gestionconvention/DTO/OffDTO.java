package com.arabsoft.gestionconvention.DTO;

import com.arabsoft.gestionconvention.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
public class OffDTO {
    @JsonProperty("pCodSoc")
    private String pCodSoc;

    @JsonProperty("pMatPers")
    private String pMatPers;

    @JsonProperty("pEtatOffDem")
    private String pEtatOffDem;

    @JsonProperty("pDatDemConv")
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate pDatDemConv;

    @JsonProperty("pDatOffDem")
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate pDatOffDem;
}