package com.arabsoft.Credits.Entities.Cles;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ClePiecePRetPers implements Serializable {
    private String cod_soc;
    private String  mat_pers;
    private Long cod_pret;
    private String  cod_piece_pret;
}
