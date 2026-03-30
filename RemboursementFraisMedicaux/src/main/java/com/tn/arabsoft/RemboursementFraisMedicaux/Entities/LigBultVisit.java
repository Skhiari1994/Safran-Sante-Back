package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.RemboursementFraisMedicaux.Configuration.CustomLocalDateDeserializer;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.CleLigBultVisit;
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
@IdClass(CleLigBultVisit.class)
public class LigBultVisit {
    @Id
    private String cod_soc;
    @Id
    private String   mat_pers;
    @Id
    private String   num_fam;
    @Id
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_soin;
    @Id
    private String    abrv_act;
    @Id
    private String   cod_visit;
    private Long   num_lig;
    private BigDecimal mnt_honor;
    private BigDecimal  mnt_remb;
    private BigDecimal   mnt_net;
    private Long  indice;
    @Id
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_act;
    private String  prf_typ;
    private String  prf_cod;
    private BigDecimal  prix_visit;
    private BigDecimal  taux_remb;
    private BigDecimal mut_mnt_net;

}
