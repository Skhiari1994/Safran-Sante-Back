package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.AssurFilCle;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "assur_fil")
@IdClass(AssurFilCle.class)
@SuppressWarnings({ "java:S116" })
public class AssurFil {

    @Id
    private String cod_assur;

    @Id
    private String cod_fil;

    private BigDecimal mnt_adher;

    private BigDecimal mnt_enf;

    private BigDecimal mnt_conj;

    private BigDecimal mnt_pere;

    private BigDecimal mnt_mere;

    private String prorat_pec;

}
