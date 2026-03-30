package com.arabsoft.referentiel.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ACTIVITE_ETABLIS")
public class ActiviteEtablis {
    @Id
    private String cod_activ;
    private String  lib_activite;
    private String  lib_activite_a;

}
