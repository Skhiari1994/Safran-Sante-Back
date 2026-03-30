package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.RemboursementFraisMedicaux.Configuration.CustomLocalDateDeserializer;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.CleLigBultAct;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.CleLigBultMed;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@IdClass(CleLigBultMed.class)
public class LigBultMed {
    @Id
   private String cod_soc;
    @Id
    private String    mat_pers;
    @Id
    private String   num_fam;
    @Id
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_soin;
    @Id
    private String    abrv_act;
    @Id
    private String   cod_med;
    private Long   num_lig;
    private Long    indice;
    private BigDecimal mnt_honor;
    private BigDecimal  mnt_net;
    private BigDecimal  mnt_remb;
    private String  accord_med;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_act;
    private String  prf_typ;
    private String  prf_cod;
    private String   num_pec_med;
    private BigDecimal  mdc_prix;
    private BigDecimal  med_prix;
    private BigDecimal  prix_remb;
    @Id
    private Long  num_lig_med;
    private BigDecimal  mut_mnt_net;
    private Long  nbr_j;

}
