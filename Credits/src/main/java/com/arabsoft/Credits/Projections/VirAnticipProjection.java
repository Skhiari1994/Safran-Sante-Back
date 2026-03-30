package com.arabsoft.Credits.Projections;

import com.arabsoft.Credits.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface VirAnticipProjection {

         String getCod_soc();
        String getMat_pers();
        String getNum_vir();
        @JsonDeserialize(using = CustomLocalDateDeserializer.class)
        LocalDate getDat_anticip();
        BigDecimal getMont_vir();
        BigDecimal getMont_antic();
        BigDecimal getRest_vir();
        String getEtat_vir();
        String getImput_cpt();
        String getSeq_ecrt();
        String getRef_metier();
        String getObs_metier();
        BigDecimal getMnt_esp();
        String getNom();


}
