package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReponseGetPlafondMutuelle {

    String soldPlafond;
    String soldPlafondEstim;
    String ageAn;
    String ageMois;
    String message;
}
