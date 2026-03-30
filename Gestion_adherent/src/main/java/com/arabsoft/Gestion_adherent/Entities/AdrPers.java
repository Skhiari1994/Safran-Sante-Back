package com.arabsoft.Gestion_adherent.Entities;

import com.arabsoft.Gestion_adherent.Entities.Cle.CleAdrPers;
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
@IdClass(CleAdrPers.class)
public class AdrPers {
    private String cod_gouv;
    private String cod_poste;
    @Id
    private String cod_soc;
    @Id
    private String mat_pers;
    @Id
    private Long num_adr;
    private String  rue;
    private String  tel_pers;
    private String  fax_pers;
    private String  adr_courant;
    private String rue_a;

}
