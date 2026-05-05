package com.arabsoft.reports.entities.cle;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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
