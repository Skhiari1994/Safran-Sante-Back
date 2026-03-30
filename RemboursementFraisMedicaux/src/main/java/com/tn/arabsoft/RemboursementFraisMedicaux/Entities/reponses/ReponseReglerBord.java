package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class ReponseReglerBord {

    BigDecimal wtot_remb;
    BigDecimal wtot_remb_aff;
    String wreg_bord;
    String message;
}
