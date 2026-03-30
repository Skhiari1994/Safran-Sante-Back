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
public class Maladie {
    @Id
    private String  cod_malad;
    private String   lib_malad;
    private String   apci;
    private String  lib_malad_a;
    private BigDecimal mnt_seuil;

}
