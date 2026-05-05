package com.arabsoft.referentiel.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "maladie")
@ToString
@SuppressWarnings({ "java:S116" })
public class Maladie {

    @Id
    private String cod_malad;

    private String lib_malad;

    private String lib_malad_a;

    private String apci;

    private BigDecimal mnt_seuil;

}
