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
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "param_caisse")
@ToString
@SuppressWarnings({ "java:S116" })
public class ParamCaisse {

    @Id
    private String cod_param;

    private BigDecimal mnt_init_cotis;

    private BigDecimal mnt_cotis;

    private BigDecimal mnt_aug_cotis;

    private Long nbr_ann_aug;

    private BigDecimal mnt_init_prim;

    private BigDecimal mnt_prime;

    private BigDecimal mnt_aug_prim;

    private Long nbr_aug_prim;

    private LocalDate dat_der_maj;

    private LocalDate dat_fin_maj;

}
