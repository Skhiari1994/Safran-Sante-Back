package com.arabsoft.referentiel.Entities.Cle;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class CleRefFillAct  implements Serializable {
    private String cod_fil;
    private String   abrv_act;
}
