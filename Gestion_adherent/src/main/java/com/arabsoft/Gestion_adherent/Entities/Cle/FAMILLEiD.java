package com.arabsoft.Gestion_adherent.Entities.Cle;

import java.io.Serializable;

public class FAMILLEiD implements Serializable {
    private static final Long serialVersionUID = 1L;
    private String cod_soc;
    private String mat_pers;
    private Long num_fam;

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

    public Long getNum_fam() {
        return num_fam;
    }

    public void setNum_fam(Long num_fam) {
        this.num_fam = num_fam;
    }
}
