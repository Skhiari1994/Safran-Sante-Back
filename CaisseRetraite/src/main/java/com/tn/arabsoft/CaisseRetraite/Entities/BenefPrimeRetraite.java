package com.tn.arabsoft.CaisseRetraite.Entities;

import com.tn.arabsoft.CaisseRetraite.Entities.Cles.CleBenefPrimeRetr;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.math.BigDecimal;

@Entity
@IdClass(CleBenefPrimeRetr.class)
public class BenefPrimeRetraite {
    @Id
    private String cod_soc;
    @Id
    private String   mat_pers;
    @Id
    private Long num_remb;
    @Id
    private Long  num_fam;
    private String  nom_benef;
    private BigDecimal montant_benef;
    private String num_cheq;


    public BenefPrimeRetraite() {
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

    public Long getNum_remb() {
        return num_remb;
    }

    public void setNum_remb(Long num_remb) {
        this.num_remb = num_remb;
    }

    public Long getNum_fam() {
        return num_fam;
    }

    public void setNum_fam(Long num_fam) {
        this.num_fam = num_fam;
    }

    public String getNom_benef() {
        return nom_benef;
    }

    public void setNom_benef(String nom_benef) {
        this.nom_benef = nom_benef;
    }

    public BigDecimal getMontant_benef() {
        return montant_benef;
    }

    public void setMontant_benef(BigDecimal montant_benef) {
        this.montant_benef = montant_benef;
    }

    public String getNum_cheq() {
        return num_cheq;
    }

    public void setNum_cheq(String num_cheq) {
        this.num_cheq = num_cheq;
    }
}
