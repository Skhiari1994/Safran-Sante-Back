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
public class ReponseCloture {

    private String message;

    private String valid_bord;

}
