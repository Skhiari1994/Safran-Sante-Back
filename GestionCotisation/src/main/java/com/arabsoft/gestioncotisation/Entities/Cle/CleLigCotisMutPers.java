package com.arabsoft.gestioncotisation.Entities.Cle;

import com.arabsoft.gestioncotisation.Configuration.CustomLocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

public class CleLigCotisMutPers implements Serializable {
    private static final Long serialVersionUID = 1L;
    public String cod_soc;
    public String mat_pers;
    public Long num_cot;
    private LocalDate dat_cot;


    // Getter and Setter for cod_soc
    public String getCod_soc() {
        return cod_soc;
    }

    public void setCod_soc(String cod_soc) {
        this.cod_soc = cod_soc;
    }

    // Getter and Setter for mat_pers
    public String getMat_pers() {
        return mat_pers;
    }

    public void setMat_pers(String mat_pers) {
        this.mat_pers = mat_pers;
    }

    // Getter and Setter for num_cot
    public Long getNum_cot() {
        return num_cot;
    }

    public void setNum_cot(Long num_cot) {
        this.num_cot = num_cot;
    }

    // Getter and Setter for dat_cot
    public LocalDate getDat_cot() {
        return dat_cot;
    }

    public void setDat_cot(LocalDate dat_cot) {
        this.dat_cot = dat_cot;
    }
}
