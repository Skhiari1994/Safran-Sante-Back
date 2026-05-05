package com.arabsoft.gestion_adherent.entities.cle;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@SuppressWarnings({ "java:S116" })
public class CleAffilMutuelle {

    private String cod_soc;

    private String mat_pers;

    private LocalDate dat_ass;

}
