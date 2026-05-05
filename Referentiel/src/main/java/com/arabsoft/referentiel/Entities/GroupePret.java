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
@Table(name = "groupe_pret")
@ToString
@SuppressWarnings({ "java:S116" })
public class GroupePret {

    private String cod_soc;

    @Id
    private String cod_grp_pret;

    private String lib_grp_pret;

    private String lib_grp_pret_a;

    private String typ_groupe;

}
