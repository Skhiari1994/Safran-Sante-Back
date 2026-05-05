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
@Table(name = "ref_act")
@ToString
@SuppressWarnings({ "java:S116" })
public class RefAct {

    @Id
    private String cod_act;

    private String lib_act;

    private String let_cod;

    private Long cot_act;

    private BigDecimal act_prix;

    private String nat_act;

    private String abrv_act;

    private BigDecimal taux_act;

}
