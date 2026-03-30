package com.arabsoft.Credits.Entities;

import com.arabsoft.Credits.Entities.Cles.CleCommission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(CleCommission.class)
public class Commission {
    @Id
    private String cod_soc;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqComm")
    @SequenceGenerator(name = "seqComm", sequenceName = "SEQ_COMMISSION", allocationSize = 1)
    private Long num_comm	;
    private LocalDate dat_comm;
    private String   ref_comm;
    private LocalDate   dat_deb_comm;
    private LocalDate   dat_fin_comm;
    private LocalDate  dat_real;
    private String   cod_etat_pret;
    private String  typ_etat;
    private String  observ;

}
