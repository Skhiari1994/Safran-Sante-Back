package com.arabsoft.referentiel.entities;

import com.arabsoft.referentiel.entities.cle.ClePoste;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@Table(name = "poste")
@ToString
@IdClass(ClePoste.class)
@SuppressWarnings({ "java:S116" })
public class Poste {

    @Id
    private String cod_gouv;

    @Id
    private String cod_poste;

    private String lib_post;

    private String lib_poste_a;

}
