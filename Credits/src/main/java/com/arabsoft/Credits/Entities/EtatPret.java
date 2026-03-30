package com.arabsoft.Credits.Entities;

import com.arabsoft.Credits.Entities.Cles.CleEtatPret;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(CleEtatPret.class)
public class EtatPret {
    @Id
    private String typ_etat;
    @Id
    private String cod_etat_pret;
    private String  lib_etat_pret;
    private String  lib_etat_pret_a	;
    private String  nat_etat_pret;
    private String ord;

}
