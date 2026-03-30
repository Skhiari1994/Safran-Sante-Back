package com.arabsoft.referentiel.Entities.Cle;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BaremRembCle implements Serializable {

    private String cod_fil;
    private String abrv_act;
    private String cod_assur;

}