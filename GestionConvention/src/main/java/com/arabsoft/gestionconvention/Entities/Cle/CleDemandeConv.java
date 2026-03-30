package com.arabsoft.gestionconvention.Entities.Cle;

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
public class CleDemandeConv  implements Serializable {

    private static final Long serialVersionUID = 1L;
    private String cod_conv;
    private String cod_soc;
    private String mat_pers;

}
