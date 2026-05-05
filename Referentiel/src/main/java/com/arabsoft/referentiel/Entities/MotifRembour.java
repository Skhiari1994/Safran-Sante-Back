package com.arabsoft.referentiel.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "motif_rembour")
@ToString
@SuppressWarnings({ "java:S116" })
public class MotifRembour {

    @Id
    private String cod_remb;

    private String lib_remb;

    private String benef_prime;

    private String benef_droi;

    private BigDecimal taux_prime;

}
