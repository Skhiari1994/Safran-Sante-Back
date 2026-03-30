package com.arabsoft.gestionconvention.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Data
public class Convention {
    @Id
    private String  cod_conv;
    private String   lib_conv;
    private String   lib_conv_a	;
    private LocalDate dat_conv;
    private String   ref_contrat;

}
