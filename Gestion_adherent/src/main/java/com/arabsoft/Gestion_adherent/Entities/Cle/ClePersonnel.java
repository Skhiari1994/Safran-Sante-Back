package com.arabsoft.Gestion_adherent.Entities.Cle;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ClePersonnel  implements Serializable {
	
	private static final long serialVersionUID = 1L;
 	private String cod_soc	;
 	private String  mat_pers;

}
