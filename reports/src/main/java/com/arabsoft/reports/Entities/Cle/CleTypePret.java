package com.arabsoft.reports.Entities.Cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CleTypePret implements Serializable {

     private String  cod_soc	;
     private String   cod_grp_pret;
     private String   typ_pret;
}
