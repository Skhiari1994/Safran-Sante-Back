package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.AssurActivCle;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "assur_activ")
@IdClass(AssurActivCle.class)
@SuppressWarnings({ "java:S116" })
public class AssurActiv {

    @Id
    private String cod_activite;

    @Id
    private String cod_assur;

    private Integer age_max;

    private Integer age_alert;

}
