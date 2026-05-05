package com.arabsoft.gestion_adherent.entities;

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
@Table(name = "type_depart")
@SuppressWarnings({ "java:S116" })
public class TypeDepart {

    @Id
    private String cod_typ_depart;

    private String lib_typ_depart;

    private String lib_typ_depart_a;

    private String etat_act;

}
