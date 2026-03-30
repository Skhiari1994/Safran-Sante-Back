package com.tn.arabsoft.CaisseRetraite.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class MotifRembour {
    @Id
   private String cod_remb;
    private String  lib_remb;
    private String  benef_prime;
    private String  benef_droi;
    private BigDecimal taux_prime;

    public MotifRembour() {
    }

    public String getCod_remb() {
        return cod_remb;
    }

    public void setCod_remb(String cod_remb) {
        this.cod_remb = cod_remb;
    }

    public String getLib_remb() {
        return lib_remb;
    }

    public void setLib_remb(String lib_remb) {
        this.lib_remb = lib_remb;
    }

    public String getBenef_prime() {
        return benef_prime;
    }

    public void setBenef_prime(String benef_prime) {
        this.benef_prime = benef_prime;
    }

    public String getBenef_droi() {
        return benef_droi;
    }

    public void setBenef_droi(String benef_droi) {
        this.benef_droi = benef_droi;
    }

    public BigDecimal getTaux_prime() {
        return taux_prime;
    }

    public void setTaux_prime(BigDecimal taux_prime) {
        this.taux_prime = taux_prime;
    }
}
