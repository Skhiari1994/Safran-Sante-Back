package com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings({ "java:S116" })
public class ReponseRegBord {

    private String reg_bord;

    private String tot_mut;

    private String message;

}
