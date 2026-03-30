package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tn.arabsoft.RemboursementFraisMedicaux.Configuration.CustomLocalDateDeserializer;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.BordEnvoiCle;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@IdClass(BordEnvoiCle.class)
public class BordEnvoi {

    @Id
    private String cod_bord;
    @Id
    private String cod_soc;
    private String cod_assur;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_bord;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_deb;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_fin;
    private Long nbr_bult;
    private BigDecimal tot_honor;
    private String typ_bord;
    private BigDecimal tot_net;
    private String valid_bord;
    private String reg_bord;
    private BigDecimal tot_remb;
    private String envoi_bord;


}
