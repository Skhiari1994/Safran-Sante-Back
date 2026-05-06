package com.tn.arabsoft.remboursement_frais_medicaux.projections;

import java.time.LocalDate;

@SuppressWarnings({ "java:S100" })
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
