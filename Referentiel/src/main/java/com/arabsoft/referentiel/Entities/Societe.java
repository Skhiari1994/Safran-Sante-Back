package com.arabsoft.referentiel.Entities;

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
public class Societe {
    @Id
    public String cod_soc;
    public String lib_soc;
    public String  cod_retr;
    public String  adr_soc;
    public String  lib_soc_a;
    public String  adr_soc_a;
    public String  tel_soc;
    public String  fax_soc;
    public String  num_retr;
    public String  rep_web;
    public String regime;
    public String num_police;

}
