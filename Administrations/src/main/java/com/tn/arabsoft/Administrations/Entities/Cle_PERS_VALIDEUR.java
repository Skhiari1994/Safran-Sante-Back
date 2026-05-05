package com.tn.arabsoft.administrations.entities;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings({ "java:S101", "java:S116" })
public class Cle_PERS_VALIDEUR implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cod_soc;

    private String mat_pers;

    private String mat_resp;

}
