package com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@SuppressWarnings({ "java:S116" })
public class ReponseCalculMntNet {

    BigDecimal p_cumul_net;

    BigDecimal p_mnt_net;

    String message;

}
