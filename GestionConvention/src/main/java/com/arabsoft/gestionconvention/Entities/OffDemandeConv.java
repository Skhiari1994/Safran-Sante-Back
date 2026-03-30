package com.arabsoft.gestionconvention.Entities;
import com.arabsoft.gestionconvention.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestionconvention.Entities.Cle.CleOffDemandeConv;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@IdClass(CleOffDemandeConv.class)
public class OffDemandeConv {
    @Id
    private String cod_conv;
    @Id
    private String  cod_soc	;
    @Id
    private String   mat_pers	;
    @Id
    private String   cod_off;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)

    private LocalDate dat_off_dem;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)

    private LocalDate   dat_fin_off;
    private BigDecimal mnt_off;
    private String  etat_off_dem;
    private String  cod_user;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)

    private LocalDate  dat_saisie;
    @Id
    private Long  seq;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)

    private LocalDate  dat_susp;
    private String  num_tel;
    private String cod_mot_susp	;
    private String   renouv;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)

    private LocalDate   dat_renouv;
    private String  obs_off;

}
