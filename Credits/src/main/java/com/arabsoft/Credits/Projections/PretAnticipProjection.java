package com.arabsoft.Credits.Projections;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PretAnticipProjection {

    String getCod_pret();
    String getTyp_pret();
    String getLib_pret();
    BigDecimal getPrt_mnt_rem();
    String getPrt_ech();
    BigDecimal getPrt_rendu();
    BigDecimal getRem_men();
    String getCod_grp_pret();
    String getPrt_dat_deb();
    String getPrt_dat_fin();
    String getNbr_retenue();
    LocalDate getDat_effet();


}
