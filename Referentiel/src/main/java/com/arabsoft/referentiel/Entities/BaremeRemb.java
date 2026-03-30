package com.arabsoft.referentiel.Entities;

import com.arabsoft.referentiel.Configurations.CustomLocalDateDeserializer;
import com.arabsoft.referentiel.Entities.Cle.BaremRembCle;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "bareme_remb")
@IdClass(BaremRembCle.class)
public class BaremeRemb {
    @Id
    private String cod_fil;
    @Id
    private String abrv_act;
    @Id
    private String cod_assur;
    private String a_indice;
    private BigDecimal mtt_acte;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_acte;
    private BigDecimal taux_act;
    private String plafonne;
    private BigDecimal plafond;
    private String verif_piece;
    private String nat_act;
    private String verif_vign;
    private String duree_act;
    private String plafon_prest;

}
