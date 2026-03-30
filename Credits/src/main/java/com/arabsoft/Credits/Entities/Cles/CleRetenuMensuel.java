package com.arabsoft.Credits.Entities.Cles;

import java.io.Serializable;
import java.time.LocalDate;

public class CleRetenuMensuel implements Serializable {

    private String cod_soc;
    private LocalDate mois_retenue;
    private String  mat_pers;
    private String  abrv_fixe;
}
