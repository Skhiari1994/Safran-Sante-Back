package com.arabsoft.referentiel.entities;

import com.arabsoft.referentiel.entities.cle.ClePositionPret;

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
@Table(name = "position_pret")
@ToString
@IdClass(ClePositionPret.class)
@SuppressWarnings({ "java:S116" })
public class PositionPret {

    @Id
    private String cod_grp_pret;

    @Id
    private String typ_pret;

    @Id
    private String cod_motif;

    private String cod_soc;

}
