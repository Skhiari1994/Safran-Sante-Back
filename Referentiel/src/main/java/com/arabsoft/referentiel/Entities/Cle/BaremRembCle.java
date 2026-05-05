package com.arabsoft.referentiel.entities.cle;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuppressWarnings({ "java:S116" })
public class BaremRembCle implements Serializable {

    private String cod_fil;
    private String abrv_act;
    private String cod_assur;

}