package com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReponseRegBord {
    private String reg_bord;
    private String tot_mut;
    private String message;

    public ReponseRegBord(String reg_bord, String tot_mut, String message) {
        this.reg_bord = reg_bord;
        this.tot_mut = tot_mut;
        this.message = message;
    }

}
