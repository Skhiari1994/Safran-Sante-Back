package com.arabsoft.gestioncotisation.Entities;

import com.arabsoft.gestioncotisation.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestioncotisation.DTO.CotisDto;
import com.arabsoft.gestioncotisation.Entities.Cle.CotisMutPersID;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(CotisMutPersID.class)
@Table(name="COTIS_MUT_PERS")
public class CotisMutPers {

    @Id
    private String cod_soc;
    @Id
    private String mat_pers;
    @Id
    private Long num_cot;
    private String typ_cot;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_deb;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_fin;

    private BigDecimal mnt_a_payer;
    private BigDecimal mnt_payer;
    private String mod_pay;
    private String ref_pay;

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_saisie;

    private String etat_cot;
    private String corps;
    private String cod_typ_depart;
    private String cod_affect;
    private BigDecimal mnt_param;
    private String num_retr;
    private Long seq_ecrt1;   // Changed from float to Float
    private Long seq_ecrt2;   // Changed from float to Float
    private String imput;


    public void updateFromDto(CotisDto dto) {
        this.cod_affect = dto.getCod_affect();
        this.cod_typ_depart = dto.getCod_typ_depart();
        this.corps = dto.getCorps();
        this.dat_deb = dto.getDat_deb();
        this.dat_fin = dto.getDat_fin();
        this.dat_saisie = dto.getDat_saisie();
        this.etat_cot = dto.getEtat_cot();
        this.imput = dto.getImput();
        this.mnt_a_payer = dto.getMnt_a_payer();
        this.mnt_param = dto.getMnt_param();
        this.mnt_payer = dto.getMnt_payer();
        this.mod_pay = dto.getMod_pay();
        this.num_retr = dto.getNum_retr();
        this.ref_pay = dto.getRef_pay();
        this.seq_ecrt1 = dto.getSeq_ecrt1();
        this.seq_ecrt2 = dto.getSeq_ecrt2();
        this.typ_cot = dto.getTyp_cot();
    }
}
