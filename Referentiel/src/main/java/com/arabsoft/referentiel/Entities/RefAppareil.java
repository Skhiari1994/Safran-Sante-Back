package com.arabsoft.referentiel.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class RefAppareil {

    @Id
    private String  cod_app;
    private String  lib_app;
    private String  lib_app_a;
    private String  abrv_act;
    private BigDecimal prix_app;
    private String    mois;
    private String   devis;
    private Long   duree_app;

}
