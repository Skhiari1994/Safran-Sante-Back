package com.tn.arabsoft.remboursement_frais_medicaux.dto;

import java.util.List;

import lombok.Data;

@Data
public class BordereauRequest {

    private String codBord;

    private String codSoc;

    private List<BulletinDTO> bulletins;

}
