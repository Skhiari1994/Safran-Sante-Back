package com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings({ "java:S116" })
public class ReponseReglerBord {

    BigDecimal wtot_remb;

    BigDecimal wtot_remb_aff;

    String wreg_bord;

    String message;

}
