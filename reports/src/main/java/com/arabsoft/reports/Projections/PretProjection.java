package com.arabsoft.reports.Projections;

import java.math.BigDecimal;

public interface PretProjection {

    String getTyp_pret();
    String getCod_pret();
    String getDate_deb();  // formatée en dd/MM/yyyy
    BigDecimal getPrt_mnt_glb();
    String getLib_pret();
}
