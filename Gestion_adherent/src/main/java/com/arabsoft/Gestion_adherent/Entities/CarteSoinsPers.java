package com.arabsoft.Gestion_adherent.Entities;

import com.arabsoft.Gestion_adherent.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.Gestion_adherent.Entities.Cle.CleCarteSoinsPers;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(CleCarteSoinsPers.class)
public class CarteSoinsPers {
    @Id
    private String cod_soc;
    @Id
    private String  mat_pers;
    @Id
    private String   num_fam;
    @Id
    private String  nat_cart;
    @Id
    private String  num_cart;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_cart;
    private String  etat_cart;
    private String obs_cart;
    private String  perdu;

}
