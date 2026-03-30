package com.arabsoft.gestioncotisation.Projections;

import com.arabsoft.gestioncotisation.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CotisMutPersProjection {

    String getCod_soc();

    String getMat_pers();

    String getNom_pers();

    Long getNum_cot();
    String getTyp_cot();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_deb();
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_fin();

    BigDecimal getMnt_a_payer();
    BigDecimal getMnt_payer();
    String getMod_pay();
    String getRef_pay();

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    LocalDate getDat_saisie();

    String getEtat_cot();
    String getCorps();
    String getCod_typ_depart();
    String getCod_affect();
    BigDecimal getMnt_param();
    String getNum_retr();
    Long getSeq_ecrt1();   // Changed from float to Float
    Long getSeq_ecrt2();   // Changed from float to Float
    String getImput();


}
