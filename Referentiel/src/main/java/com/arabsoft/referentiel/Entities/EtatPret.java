package com.arabsoft.referentiel.entities;

import com.arabsoft.referentiel.entities.cle.CleEtatPret;
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
@Table(name = "etat_pret")
@ToString
@IdClass(CleEtatPret.class)
@SuppressWarnings({ "java:S116" })
public class EtatPret {
    @Id
    private String typ_etat;
    @Id
    private String cod_etat_pret;
    private String lib_etat_pret;
    private String lib_etat_pret_a;
    private String nat_etat_pret;
    private String ord;

}
