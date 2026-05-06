package com.tn.arabsoft.remboursement_frais_medicaux.entities;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.ClePersAffil;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pers_affil")
@IdClass(ClePersAffil.class)
@SuppressWarnings({ "java:S116" })
public class PersAffil {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private Long annee_aff;

    private String cod_fil;

    private String courant;

    private String prf_typ;

    private String prf_cod;

}
