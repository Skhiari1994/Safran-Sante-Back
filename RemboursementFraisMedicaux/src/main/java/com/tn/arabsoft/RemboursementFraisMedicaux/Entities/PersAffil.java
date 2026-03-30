package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.ClePersAffil;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Data
@Entity
@IdClass(ClePersAffil.class)
public class PersAffil {

    @Id
    private String cod_soc;
    @Id
    private String   mat_pers;
    private String  cod_fil;
    private String  courant	;
    @Id
    private Long   annee_aff;
    private String  prf_typ;
    private String   prf_cod;

}
