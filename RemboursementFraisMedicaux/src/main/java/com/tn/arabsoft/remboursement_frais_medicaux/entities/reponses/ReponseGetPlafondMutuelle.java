package com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReponseGetPlafondMutuelle {

    String soldPlafond;

    String soldPlafondEstim;

    Integer ageAn;

    Integer ageMois;

    String message;

}
