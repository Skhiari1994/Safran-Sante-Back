package com.arabsoft.referentiel.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "types_actes")
public class TypesActes {

    @Id
    private String type_acte;
    private String lib_type_acte;
    private String lib_type_acte_a;

}
