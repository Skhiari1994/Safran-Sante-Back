package com.arabsoft.referentiel.entities;

import com.arabsoft.referentiel.entities.cle.AssurFilCle;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "assur_fil")
@ToString
@IdClass(AssurFilCle.class)
@SuppressWarnings({ "java:S116" })
public class AssurFil {

    @Id
    @JsonProperty("cod_assur")
    private String cod_assur;

    @Id
    @JsonProperty("cod_fil")
    private String cod_fil;

    @JsonProperty("mnt_adher")
    private BigDecimal mnt_adher;

    @JsonProperty("mnt_enf")
    private BigDecimal mnt_enf;

    @JsonProperty("mnt_conj")
    private BigDecimal mnt_conj;

    @JsonProperty("mnt_pere")
    private BigDecimal mnt_pere;

    @JsonProperty("mnt_mere")
    private BigDecimal mnt_mere;

    @JsonProperty("prorat_pec")
    private String prorat_pec;

}
