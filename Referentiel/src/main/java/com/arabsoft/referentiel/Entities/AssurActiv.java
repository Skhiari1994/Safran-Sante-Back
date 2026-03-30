package com.arabsoft.referentiel.Entities;

import com.arabsoft.referentiel.Entities.Cle.AssurActivCle;
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
@ToString
@Table(name = "assur_activ")
@IdClass(AssurActivCle.class)
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
