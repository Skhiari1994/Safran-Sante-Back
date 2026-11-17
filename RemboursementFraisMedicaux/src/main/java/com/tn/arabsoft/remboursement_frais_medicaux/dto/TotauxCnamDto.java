package com.tn.arabsoft.remboursement_frais_medicaux.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TotauxCnamDto(@JsonProperty("xmnt_honor") BigDecimal xmntHonor,
        @JsonProperty("ymnt_tot") BigDecimal ymntTot,
        @JsonProperty("zMNT_TOT_MUT") BigDecimal zMntTotMut,
        @JsonProperty("asold") BigDecimal asold) {

}
