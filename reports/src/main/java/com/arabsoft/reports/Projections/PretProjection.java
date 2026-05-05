package com.arabsoft.reports.projections;

import java.math.BigDecimal;

@SuppressWarnings({ "java:S100" })
public interface PretProjection {

    String getTyp_pret();

    String getCod_pret();

    String getDate_deb();

    BigDecimal getPrt_mnt_glb();

    String getLib_pret();

}
