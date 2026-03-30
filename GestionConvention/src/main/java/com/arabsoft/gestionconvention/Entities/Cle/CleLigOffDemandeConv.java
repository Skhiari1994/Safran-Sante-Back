package com.arabsoft.gestionconvention.Entities.Cle;

import com.arabsoft.gestionconvention.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
public class CleLigOffDemandeConv  implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String cod_conv;
    private String cod_soc;
    private String mat_pers;
    private String cod_off;
    private Long seq;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate mois;
}
