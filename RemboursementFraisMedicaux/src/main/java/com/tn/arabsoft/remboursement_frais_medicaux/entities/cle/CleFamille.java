package com.tn.arabsoft.remboursement_frais_medicaux.entities.cle;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings({ "java:S116" })
public class CleFamille implements Serializable {

    private String cod_soc;

    private String mat_pers;

    private Long num_fam;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CleFamille that = (CleFamille) o;
        return Objects.equals(cod_soc, that.cod_soc)
                && Objects.equals(mat_pers, that.mat_pers)
                && Objects.equals(num_fam, that.num_fam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cod_soc, mat_pers, num_fam);
    }

}
