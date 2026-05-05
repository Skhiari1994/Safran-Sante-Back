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
@Table(name = "assurance")
@ToString
@SuppressWarnings({ "java:S116" })
public class Assurance {

    @Id
    private String cod_assur;

    private String lib_assur;

    private String lib_assur_a;

    private String regime_assur;

    private String num_police;

    private String typ_plafond;

    private String tel_assur;

    private String fax_assur;

    private String prefixe;

    private BigDecimal mnt_adher;

    private BigDecimal mnt_enf;

    private BigDecimal mnt_conj;

    private BigDecimal mnt_pere;

    private BigDecimal mnt_mere;

    private String prorat_pec;

    private Integer duree_bult_mut;

    private Integer duree_bult_cnam;

    private String cod_retr;

    private String num_retr;

    private BigDecimal taux_mut;

    private BigDecimal plaf_mut;

}
