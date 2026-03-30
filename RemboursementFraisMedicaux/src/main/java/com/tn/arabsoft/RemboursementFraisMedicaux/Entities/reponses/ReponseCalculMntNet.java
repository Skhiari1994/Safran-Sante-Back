package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ReponseCalculMntNet {
    BigDecimal p_cumul_net ;
    BigDecimal  p_mnt_net;
    String  message;
}
