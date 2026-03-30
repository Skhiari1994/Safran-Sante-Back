package com.arabsoft.gestionconvention.DTO;

import lombok.Data;

@Data
public class UpdateEtatRequest { private String codSoc;
    private String codConv;
    private String matPers;
    private String codOff;
    private Long seq;
    private String etatOffDem;


}
