package com.arabsoft.gestion_adherent.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fich_sal")
@SuppressWarnings({ "java:S116" })
public class FichSal {

    @Id
    private Long ordre;
    private String ligne;
    private String typ_lig;
}
