package com.arabsoft.referentiel.Entities.Cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CleEtatPret implements Serializable {

    private String typ_etat;
    private String cod_etat_pret;
}
