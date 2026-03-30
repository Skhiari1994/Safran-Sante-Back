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
public class RefVisit {
     @Id
    private String cod_visit;
    private String  lib_visit;
    private String   cod_sec;
    private String   typ_nat;
    private String   lib_visit_a;
    private String  abrv_act;
    private BigDecimal prix_visit;
    private BigDecimal  taux_remb;

}
