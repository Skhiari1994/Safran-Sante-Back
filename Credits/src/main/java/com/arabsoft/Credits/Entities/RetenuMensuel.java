package com.arabsoft.Credits.Entities;

import com.arabsoft.Credits.Entities.Cles.CleRetenuMensuel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@IdClass(CleRetenuMensuel.class)
public class RetenuMensuel {

    @Id
    private String cod_soc;
    @Id
    private LocalDate mois_retenue;
    @Id
    private String  mat_pers;
    @Id
    private String  abrv_fixe;
    private BigDecimal mnt_retenue;
    private String   valid;
    private LocalDate   date_fin;
    private String   motif;

}
