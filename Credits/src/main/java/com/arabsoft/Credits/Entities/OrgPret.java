package com.arabsoft.Credits.Entities;

import com.arabsoft.Credits.Entities.Cles.ClePersonnel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
 public class OrgPret {
     @Id
    private String  org_pret;
    private String  typ_org	;
    private String   lib_organisme;
    private String   adr_organisme	;
    private String   cod_post	;
    private String  ville;
    private String   resp_org;
    private String   rib_org;
    private String   typ_detach;
    private String   num_cpt;
    private String   cod_tier;

}
