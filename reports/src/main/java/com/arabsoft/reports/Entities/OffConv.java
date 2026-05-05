package com.arabsoft.reports.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.arabsoft.reports.entities.cle.CleOffConv;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "off_conv")
@ToString
@IdClass(CleOffConv.class)
@SuppressWarnings({ "java:S116" })
public class OffConv {

    @Id
    private String cod_conv;

    @Id
    private String cod_off;

    private String lib_off;

    private LocalDate dat_off;

    private BigDecimal mnt_off;

    private String categ_off;

}
