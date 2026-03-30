package com.arabsoft.referentiel.Entities.Cle;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CleNatureDonMotif implements Serializable {

    private String nat_don;
    private String  cod_affect;

}
