package com.arabsoft.gestionconvention.Projections;



import com.arabsoft.gestionconvention.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface OffConvProjection {

     String getCod_conv();
     String getCod_off();
     String getLib_off();
     @JsonDeserialize(using = CustomLocalDateDeserializer.class)
     LocalDate getDat_off();
     BigDecimal getMnt_off();
     String getCateg_off();
}
