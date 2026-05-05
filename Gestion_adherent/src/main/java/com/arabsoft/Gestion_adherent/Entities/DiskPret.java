package com.arabsoft.gestion_adherent.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "disk_pret")
@SuppressWarnings({ "java:S116" })
public class DiskPret {
    private LocalDate dat_disk;
    private String pret_disk;
    private String num_retr;
    private String mat_pers;
    private BigDecimal montant;
    private String observation;
    private String valid;
    private String cod_grp_pret;
    private String typ_pret;
    private Long cod_pret;
    @Id
    private Long id;

}
