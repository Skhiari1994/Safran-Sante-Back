package com.arabsoft.gestion_adherent.entities;

import com.arabsoft.gestion_adherent.entities.cle.CleAdrPers;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@Table(name = "adr_pers")
@IdClass(CleAdrPers.class)
@SuppressWarnings({ "java:S116" })
public class AdrPers {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private Long num_adr;

    private String cod_gouv;

    private String cod_poste;

    private String rue;

    private String rue_a;

    private String tel_pers;

    private String fax_pers;

    private String adr_courant;

}