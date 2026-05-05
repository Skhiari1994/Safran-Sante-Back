package com.tn.arabsoft.remboursement_frais_medicaux.entities.cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@SuppressWarnings({ "java:S116" })
public class CleRefEtablis implements Serializable {

    private String prf_typ;

    private String prf_cod;

}
