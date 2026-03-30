package com.arabsoft.Credits.Entities;

import com.arabsoft.Credits.Entities.Cles.CleVirAnticip;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(CleVirAnticip.class)
public class VirAnticip {
    @Id
    private String cod_soc;
    @Id
    private String mat_pers;
    @Id
    private Long  num_vir;
    private LocalDate dat_anticip;
    private BigDecimal mont_vir;
    private BigDecimal mont_antic;
    private BigDecimal  rest_vir;
    private String  etat_vir;
    private String  imput_cpt;
    private Long  seq_ecrt;
    private String ref_metier;
    private String  obs_metier;
    private BigDecimal  mnt_esp;

}
