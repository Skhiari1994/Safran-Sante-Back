package com.tn.arabsoft.CaisseRetraite.Entities;

import java.math.BigDecimal;

public class PrimeRetraiteDTO {
    private String cod_soc;
    private String mat_pers;
    private Long num_remb;
    private String num_retr;
    private String cod_remb;
    private String dat_remb; // String instead of LocalDate
    private String benef_prime;
    private String benef_droi;
    private BigDecimal taux_prime;
    private String dat_deb_ret; // String instead of LocalDate
    private String dat_fin_ret; // String instead of LocalDate
    private String etat_prime;
    private BigDecimal montant_cotis;
    private BigDecimal montant_prime;
    private BigDecimal montant_total;
    private String mod_pay;
    private String obs_retraite;
    private String imput_cpt;
    private Long seq_ecrt;
    private String cheq_remb;

    // Getters and Setters
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

    public Long getNum_remb() {
        return num_remb;
    }

    public void setNum_remb(Long num_remb) {
        this.num_remb = num_remb;
    }

    public String getNum_retr() {
        return num_retr;
    }

    public void setNum_retr(String num_retr) {
        this.num_retr = num_retr;
    }

    public String getCod_remb() {
        return cod_remb;
    }

    public void setCod_remb(String cod_remb) {
        this.cod_remb = cod_remb;
    }

    public String getDat_remb() {
        return dat_remb;
    }

    public void setDat_remb(String dat_remb) {
        this.dat_remb = dat_remb;
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

    public String getDat_deb_ret() {
        return dat_deb_ret;
    }

    public void setDat_deb_ret(String dat_deb_ret) {
        this.dat_deb_ret = dat_deb_ret;
    }

    public String getDat_fin_ret() {
        return dat_fin_ret;
    }

    public void setDat_fin_ret(String dat_fin_ret) {
        this.dat_fin_ret = dat_fin_ret;
    }

    public String getEtat_prime() {
        return etat_prime;
    }

    public void setEtat_prime(String etat_prime) {
        this.etat_prime = etat_prime;
    }

    public BigDecimal getMontant_cotis() {
        return montant_cotis;
    }

    public void setMontant_cotis(BigDecimal montant_cotis) {
        this.montant_cotis = montant_cotis;
    }

    public BigDecimal getMontant_prime() {
        return montant_prime;
    }

    public void setMontant_prime(BigDecimal montant_prime) {
        this.montant_prime = montant_prime;
    }

    public BigDecimal getMontant_total() {
        return montant_total;
    }

    public void setMontant_total(BigDecimal montant_total) {
        this.montant_total = montant_total;
    }

    public String getMod_pay() {
        return mod_pay;
    }

    public void setMod_pay(String mod_pay) {
        this.mod_pay = mod_pay;
    }

    public String getObs_retraite() {
        return obs_retraite;
    }

    public void setObs_retraite(String obs_retraite) {
        this.obs_retraite = obs_retraite;
    }

    public String getImput_cpt() {
        return imput_cpt;
    }

    public void setImput_cpt(String imput_cpt) {
        this.imput_cpt = imput_cpt;
    }

    public Long getSeq_ecrt() {
        return seq_ecrt;
    }

    public void setSeq_ecrt(Long seq_ecrt) {
        this.seq_ecrt = seq_ecrt;
    }

    public String getCheq_remb() {
        return cheq_remb;
    }

    public void setCheq_remb(String cheq_remb) {
        this.cheq_remb = cheq_remb;
    }
}
