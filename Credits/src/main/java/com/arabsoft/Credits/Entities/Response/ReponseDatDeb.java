package com.arabsoft.Credits.Entities.Response;

import com.arabsoft.Credits.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
public class ReponseDatDeb {
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate datFin;
     private String message;
}
