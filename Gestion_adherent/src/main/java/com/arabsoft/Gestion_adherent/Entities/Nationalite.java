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
@Table(name = "nationalite")
@SuppressWarnings({ "java:S116" })
public class Nationalite {

    @Id
    private String cod_natp;

    private String lib_nat;

    private String lib_nat_a;

}
