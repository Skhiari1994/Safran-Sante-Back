package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.CleDossierMld;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@IdClass(CleDossierMld.class)
public class DossierMld {

    @Id
    private String cod_soc;
    @Id
    private String  mat_pers;
    @Id
    private String num_dos_mld;
    private String  cod_malad;
    private LocalDate dat_doss_mld;
    private String  etat_dos_mld;
    private Long  num_fam;

}
