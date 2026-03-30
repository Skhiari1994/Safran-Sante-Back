package com.arabsoft.reports.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Data
public class NatureDon {
    @Id
   private String nat_don;
    private String lib_nat_don;
    private String  lib_nat_don_a;
    private String   typ_don;
    private String   debut_cycle;
    private String  fin_cycle;
    private Long   frequence;
    private String    limit_depot;
    private String cod_fond;
    private BigDecimal max_mnt_don;
    private String  mnt_variable;
    private String  concerne;
    private String min_mnt_don;
    private String cod_benif;
    private Long diff_period;
    private String  compt_charge;

}
