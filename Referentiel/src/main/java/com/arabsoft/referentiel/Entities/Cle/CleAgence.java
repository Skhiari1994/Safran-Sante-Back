package com.arabsoft.referentiel.Entities.Cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CleAgence implements Serializable {
    private String cod_banq;
    private String cod_agc;
}
