package com.arabsoft.referentiel.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "types_actes")
@ToString
@SuppressWarnings({ "java:S116" })
public class TypesActes {

    @Id
    @Column(name = "type_acte")
    @JsonProperty("type_acte") // Garantit que le format JSON en entrée/sortie reste inchangé pour le frontend
    private String typeActe;

    private String lib_type_acte;

    private String lib_type_acte_a;

}
