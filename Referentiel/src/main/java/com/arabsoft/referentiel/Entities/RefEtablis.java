package com.arabsoft.referentiel.Entities;

import com.arabsoft.referentiel.Configurations.CustomLocalDateDeserializer;
import com.arabsoft.referentiel.Entities.Cle.CleRefEtablis;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@IdClass(CleRefEtablis.class)
public class RefEtablis {
    @Id
   private String prf_typ;
    @Id
    private String prf_cod	;
    private String   prf_cle;
    private String   etab_rsoc;
    private String   pr_rsoc;
    private String   etab_rsoc_a;
    private String   pr_rsoc_a;
    private String  met_etab;
    private String   comp_etab;
    private String  adr_etablis	;
    private String  cod_post;
    private String  cod_gouv;
    private String   conv_etab;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_conv_etab;
    private String  rib_etab;
    private String   ref_conv_etat;
    private String   adr_etablis_a	;
    private String  cod_activ;
    private String  conv_cnam;
    private String  tel_etablis	;
    private String  fax_etablis;
    private String  email_etablis;
    private String  resp;
    private String   resp_a;
    private String  etat_act_mut;
    private String  etat_act_cnam;

}
