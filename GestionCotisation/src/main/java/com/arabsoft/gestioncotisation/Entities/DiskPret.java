package com.arabsoft.gestioncotisation.Entities;

import com.arabsoft.gestioncotisation.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
@Table(name="DISK_PRET")

public class DiskPret {

    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_disk;
    public String pret_disk;
    public String num_retr;
    public String mat_pers;
    public BigDecimal montant;
    public String observation;
    public String valid;
    public String cod_grp_pret;
    public String typ_pret;
    public Long cod_pret;
    @Id
    public Long id;
}
