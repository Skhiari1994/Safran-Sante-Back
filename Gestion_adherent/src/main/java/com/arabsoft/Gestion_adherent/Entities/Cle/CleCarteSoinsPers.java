package com.arabsoft.Gestion_adherent.Entities.Cle;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CleCarteSoinsPers implements Serializable {

    private String cod_soc;
    private String  mat_pers;
    private String   num_fam;
    private String  nat_cart;
    private String  num_cart;
}
