package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "BORD_ARRIVER")
public class BordArriver {

    @Id
    private String cod_bord;
    private String cod_assur;
    private LocalDate dat_bord;
    private LocalDate dat_deb;
    private LocalDate dat_fin;
    private Integer nbr_bult;
    private BigDecimal tot_honor;
    private String typ_bord;
    private String cod_soc;
    private BigDecimal tot_net;
    private String valid_bord;
    private String reg_bord;
    private BigDecimal tot_remb;
    private String valid;
    private String clot_bord;
    private String tot;
    private String num_retr;
    private String cod_bord_cnam;
    private Integer seq_bord;

}
