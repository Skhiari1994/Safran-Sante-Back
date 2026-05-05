package com.arabsoft.referentiel.entities.cle;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuppressWarnings({ "java:S116" })
public class CleTypePret implements Serializable {

     private String cod_soc;

     private String cod_grp_pret;

     private String typ_pret;

}
