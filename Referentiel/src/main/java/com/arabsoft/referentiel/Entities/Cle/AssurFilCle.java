package com.arabsoft.referentiel.Entities.Cle;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter

public class AssurFilCle implements Serializable {
    private String cod_assur;
    private String cod_fil;
}
