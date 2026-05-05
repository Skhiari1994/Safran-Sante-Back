package com.arabsoft.referentiel.entities.cle;

import java.io.Serializable;
import java.time.LocalDate;

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
public class LigBultCle implements Serializable {

    private String cod_soc;

    private String mat_pers;

    private String num_fam;

    private LocalDate dat_soin;

    private String abrv_act;

}
