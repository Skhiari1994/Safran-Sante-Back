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
public class RefAct {

    @Id
    private String cod_act;
    private String    lib_act;
    private String   let_cod;
    private Long   cot_act;
    private BigDecimal act_prix;
    private String   nat_act;
    private String   abrv_act;
    private BigDecimal   taux_act;

}
