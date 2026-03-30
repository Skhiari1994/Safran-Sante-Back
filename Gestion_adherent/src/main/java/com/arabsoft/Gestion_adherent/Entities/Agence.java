package com.arabsoft.Gestion_adherent.Entities;

import com.arabsoft.Gestion_adherent.Entities.Cle.CleAgence;
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
@IdClass(CleAgence.class)
public class Agence {
     @Id
    private String cod_banq;
     @Id
    private String cod_agc;
    private String lib_agc;
    private String  adr_agc	;
    private String  tel_agc	;
    private String  fax_agc;
    private String cod_banq_banq;
    private String cod_agc_agc;
    private String  tel2_agc;
    private String lib_agc_a;
    private String adr_agc_a;

}
