package com.arabsoft.referentiel.entities;

import com.arabsoft.referentiel.entities.cle.AssurActivCle;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "assur_activ")
@ToString
@IdClass(AssurActivCle.class)
@SuppressWarnings({ "java:S116" })
public class AssurActiv {

    @Id
    @JsonProperty("cod_activite")
    private String cod_activite;

    @Id
    @JsonProperty("cod_assur")
    private String cod_assur;

    @JsonProperty("age_max")
    private Integer age_max;

    @JsonProperty("age_alert")
    private Integer age_alert;

}
