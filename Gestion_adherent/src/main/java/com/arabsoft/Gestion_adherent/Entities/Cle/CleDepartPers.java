package com.arabsoft.gestion_adherent.entities.cle;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings({ "java:S116" })
public class CleDepartPers {

    private String cod_soc;

    private String mat_pers;

    private LocalDate dat_depart;

}
