package com.arabsoft.Credits.Entities.Cles;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CleDetailsCommission implements Serializable{

    private String cod_soc;
    private Long num_comm;
    private String mat_pers;
    private Long  num_dem_pret;
}
