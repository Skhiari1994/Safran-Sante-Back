package com.tn.arabsoft.CaisseRetraite.Entities;

import com.tn.arabsoft.CaisseRetraite.Entities.Cles.CleLigPrimeMutPers;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Entity
@IdClass(CleLigPrimeMutPers.class)
public class LigPrimeMutPers {
    @Id
    private String cod_soc;
    @Id
    private String   mat_pers;
    @Id
    private Long   num_prime;
    @Id
    private LocalDate dat_prime;
    private BigDecimal mnt_payer;

    public LigPrimeMutPers() {
    }

    public String getCod_soc() {
        return cod_soc;
    }

    public void setCod_soc(String cod_soc) {
        this.cod_soc = cod_soc;
    }

    public String getMat_pers() {
        return mat_pers;
    }

    public void setMat_pers(String mat_pers) {
        this.mat_pers = mat_pers;
    }

    public Long getNum_prime() {
        return num_prime;
    }

    public void setNum_prime(Long num_prime) {
        this.num_prime = num_prime;
    }

    public LocalDate getDat_prime() {
        return dat_prime;
    }

    public void setDat_prime(LocalDate dat_prime) {
        this.dat_prime = dat_prime;
    }

    public BigDecimal getMnt_payer() {
        return mnt_payer;
    }

    public void setMnt_payer(BigDecimal mnt_payer) {
        this.mnt_payer = mnt_payer;
    }
}
