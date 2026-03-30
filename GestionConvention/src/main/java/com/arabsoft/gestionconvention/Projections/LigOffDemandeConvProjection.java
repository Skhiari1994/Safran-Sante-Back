package com.arabsoft.gestionconvention.Projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface LigOffDemandeConvProjection {
    String getCod_conv();
    String getCod_soc();
    String getMat_pers();
    String getCod_off();
    Long getSeq();
    String getNum_tel();
    BigDecimal getMnt_off();
    String getMod_p();
    String getNum_retr();
    LocalDate getMois();
    String getEtat_lig_off();
    String getNom();
    String getObs_off();


}
