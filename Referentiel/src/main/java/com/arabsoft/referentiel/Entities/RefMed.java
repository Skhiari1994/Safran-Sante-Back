package com.arabsoft.referentiel.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class RefMed {
    @Id
    private String cod_med;
    private String   lib_med;
    private BigDecimal mdc_prix;
    private BigDecimal   med_prix;
    private BigDecimal   prix_remb;
    private String   cat_med;
    private String   ap;
    private String    rb;
    private String   abrv_act;

}
