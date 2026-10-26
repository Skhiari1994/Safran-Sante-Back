package com.arabsoft.gestion_adherent.entities.cle;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings({ "java:S116" })
public class CleCarteSoinsCnam {

    private String cod_soc;

    private String mat_pers;

    private String nat_cart;

    private LocalDate date_debut;

}
