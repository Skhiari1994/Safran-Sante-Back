package com.arabsoft.referentiel.Entities.Cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ClePositionPret implements Serializable {

    private String  cod_grp_pret;
    private String typ_pret;
    private String  cod_motif;
}
