package com.arabsoft.referentiel.entities;

import com.arabsoft.referentiel.entities.cle.CleRefFillAct;

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
@Table(name = "ref_fill_act")
@ToString
@IdClass(CleRefFillAct.class)
@SuppressWarnings({ "java:S116" })
public class RefFillAct {

    @Id
    private String cod_fil;

    @Id
    private String abrv_act;

    private String val_prix;

}
