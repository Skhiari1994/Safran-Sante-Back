package com.arabsoft.referentiel.Entities;

 import com.arabsoft.referentiel.Configurations.CustomLocalDateDeserializer;
 import com.arabsoft.referentiel.Entities.Cle.BordEnvoiCle;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
    private String tot_honor;
    private String typ_bord;
    private String tot_net;
    private String valid_bord;
    private String reg_bord;
    private String tot_remb;
    private String envoi_bord;


}
