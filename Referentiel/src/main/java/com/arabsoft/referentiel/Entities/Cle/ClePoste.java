package com.arabsoft.referentiel.Entities.Cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ClePoste implements Serializable {
    private String cod_gouv;
    private String cod_poste;
}
