package com.arabsoft.reports.entities.cle;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@SuppressWarnings({ "java:S116" })
public class ClePretPers implements Serializable {

    private String cod_soc;

    private String mat_pers;

    private Long cod_pret;

}
