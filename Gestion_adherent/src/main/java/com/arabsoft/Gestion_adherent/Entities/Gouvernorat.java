package com.arabsoft.Gestion_adherent.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class Gouvernorat {
     @Id
    private String cod_gouv;
    private String  lib_gouv;
    private String  lib_gouv_a;

}
