package com.arabsoft.Gestion_adherent.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class TypeDepart {
    @Id
    private String cod_typ_depart;
    private String lib_typ_depart;
    private String etat_act;
    private String lib_typ_depart_a;

}
