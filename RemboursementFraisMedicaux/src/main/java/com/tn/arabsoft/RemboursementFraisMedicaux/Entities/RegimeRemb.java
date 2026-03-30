package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class RegimeRemb {
    @Id
    private String  reg_remb;
    private String   lib_remb;
    private String  lib_remb_a;
    private String  dat_accouch;
    private String  apci;
    private String   pec;
    private String  sexe;
    private String  parente;

}
