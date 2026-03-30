package com.tn.arabsoft.RemboursementFraisMedicaux.DTO;

import java.util.List;

public class BordereauRequest {
    private String codBord;
    private String codSoc;
    private List<BulletinDTO> bulletins;

    public String getCodBord() { return codBord; }
    public void setCodBord(String codBord) { this.codBord = codBord; }

    public String getCodSoc() { return codSoc; }
    public void setCodSoc(String codSoc) { this.codSoc = codSoc; }

    public List<BulletinDTO> getBulletins() { return bulletins; }
    public void setBulletins(List<BulletinDTO> bulletins) { this.bulletins = bulletins; }
}
