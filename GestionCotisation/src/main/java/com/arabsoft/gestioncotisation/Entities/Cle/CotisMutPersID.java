package com.arabsoft.gestioncotisation.Entities.Cle;

import java.io.Serializable;
import java.util.Objects;

public class CotisMutPersID implements Serializable {

    private String cod_soc;
    private String mat_pers;
    private Long num_cot;

    // ✅ No-args constructor required by JPA
    public CotisMutPersID() {}

    // ✅ Optional: constructor with fields
    public CotisMutPersID(String cod_soc, String mat_pers, Long num_cot) {
        this.cod_soc = cod_soc;
        this.mat_pers = mat_pers;
        this.num_cot = num_cot;
    }

    // ✅ Getters and setters (or use Lombok if everything works)
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

    public Long getNum_cot() {
        return num_cot;
    }

    public void setNum_cot(Long num_cot) {
        this.num_cot = num_cot;
    }

    // ✅ equals and hashCode are MANDATORY for JPA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CotisMutPersID)) return false;
        CotisMutPersID that = (CotisMutPersID) o;
        return Objects.equals(cod_soc, that.cod_soc) &&
                Objects.equals(mat_pers, that.mat_pers) &&
                Objects.equals(num_cot, that.num_cot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cod_soc, mat_pers, num_cot);
    }
}
