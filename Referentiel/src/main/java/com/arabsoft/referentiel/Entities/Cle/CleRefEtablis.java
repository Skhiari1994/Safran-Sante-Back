package com.arabsoft.referentiel.entities.cle;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuppressWarnings({ "java:S116" })
public class CleRefEtablis implements Serializable {

    private String prf_typ;

    private String prf_cod;

}
