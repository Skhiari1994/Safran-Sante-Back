package com.tn.arabsoft.RemboursementFraisMedicaux.Projections;

import java.time.LocalDate;

public interface DossierMldProjection {


        String getcod_soc();
        String getmat_pers();
        Integer getnum_dos_mld();
        String getcod_malad();
        LocalDate getdat_doss_mld();
        String getetat_dos_mld();
        Integer getnum_fam();
        String getlibMalad();
        String getnom();


}
