package com.arabsoft.Gestion_adherent.Entities;

import com.arabsoft.Gestion_adherent.Entities.Cle.ClePoste;
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
@IdClass(ClePoste.class)
public class Poste {
    @Id
    private String cod_gouv;
    @Id
    private String cod_poste;
    private String lib_post;
    private String lib_poste_a;

}
