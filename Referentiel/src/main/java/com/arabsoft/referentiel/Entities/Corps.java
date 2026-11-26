package com.arabsoft.referentiel.entities;

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
@Table(name = "corps")
@ToString
@SuppressWarnings({ "java:S116" })
public class Corps {

    @Id
    private String cod_corps;

    private String lib_corps;

    private String lib_corps_a;

}
