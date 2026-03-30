package com.tn.arabsoft.CaisseRetraite.Entities.Reponse;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ClePersonnel implements Serializable {
	
	private static final long serialVersionUID = 1L;
 	private String cod_soc	;
 	private String  mat_pers;

}
