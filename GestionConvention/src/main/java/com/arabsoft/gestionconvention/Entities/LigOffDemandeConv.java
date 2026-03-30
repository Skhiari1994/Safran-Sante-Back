package com.arabsoft.gestionconvention.Entities;

import com.arabsoft.gestionconvention.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestionconvention.Entities.Cle.CleLigOffDemandeConv;
import com.arabsoft.gestionconvention.Entities.Cle.CleOffDemandeConv;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@Entity
@IdClass(CleLigOffDemandeConv.class)
public class LigOffDemandeConv {
   @Id
   private String cod_conv;
    @Id
    private String cod_soc;
    @Id
    private String mat_pers	;
    @Id
    private String cod_off;
    @Id
    private Long   seq;
    private String  num_tel;
    private BigDecimal mnt_off;
    private String  mod_p;
    private String   num_retr;
 @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate mois;
    private String   etat_lig_off;
     private String  obs_off;

}
