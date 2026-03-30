package com.tn.arabsoft.CaisseRetraite.Entities.Reponse;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public class ReponseCalculPrimeRetr {
    private BigDecimal  p_montant_prime ;
    private BigDecimal  p_montant_cotis ;
    private BigDecimal p_montant_total;
    private LocalDate p_dat_deb_ret;
    private LocalDate p_dat_fin_ret;

    public BigDecimal getP_montant_prime() {
        return p_montant_prime;
    }

    public void setP_montant_prime(BigDecimal p_montant_prime) {
        this.p_montant_prime = p_montant_prime;
    }

    public BigDecimal getP_montant_cotis() {
        return p_montant_cotis;
    }

    public void setP_montant_cotis(BigDecimal p_montant_cotis) {
        this.p_montant_cotis = p_montant_cotis;
    }

    public BigDecimal getP_montant_total() {
        return p_montant_total;
    }

    public void setP_montant_total(BigDecimal p_montant_total) {
        this.p_montant_total = p_montant_total;
    }

    public LocalDate getP_dat_deb_ret() {
        return p_dat_deb_ret;
    }

    public void setP_dat_deb_ret(LocalDate p_dat_deb_ret) {
        this.p_dat_deb_ret = p_dat_deb_ret;
    }

    public LocalDate getP_dat_fin_ret() {
        return p_dat_fin_ret;
    }

    public void setP_dat_fin_ret(LocalDate p_dat_fin_ret) {
        this.p_dat_fin_ret = p_dat_fin_ret;
    }
}
