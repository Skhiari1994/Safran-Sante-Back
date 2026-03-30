package com.arabsoft.gestionconvention.Entities.Cle;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CleOffDemandeConv  implements Serializable {
    private static final Long serialVersionUID = 1L;

    private String cod_conv;
    private String cod_soc;
    private String mat_pers;
    private String cod_off;
    private Long seq;
}
