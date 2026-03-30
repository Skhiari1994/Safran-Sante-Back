package com.arabsoft.gestionconvention.Entities.Cle;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CleOffConv implements Serializable {
    private String cod_conv;
    private String cod_off;
}
