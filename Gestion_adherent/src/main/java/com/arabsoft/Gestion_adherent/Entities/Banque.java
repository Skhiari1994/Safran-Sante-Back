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
public class Banque {
    @Id
    private String cod_banq;
    private String lib_banq;
    private String abrv_banq;
    private String adr_banq;
    private String tel_banq;
    private String fax_banq;
    private String lib_banq_a;
    private String abrv_banq_a	;
    private String adr_banq_a;

}
