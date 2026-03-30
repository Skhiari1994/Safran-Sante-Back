package com.arabsoft.gestionconvention.Entities;

import com.arabsoft.gestionconvention.Entities.Cle.CleOffConv;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@IdClass(CleOffConv.class)
public class OffConv {
    @Id
    private String cod_conv;
    @Id
    private String cod_off;
    private String lib_off;
    private LocalDate dat_off;
    private BigDecimal   mnt_off;
    private String  categ_off;

}
