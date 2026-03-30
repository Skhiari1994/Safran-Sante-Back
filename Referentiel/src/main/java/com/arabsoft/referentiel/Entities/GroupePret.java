package com.arabsoft.referentiel.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class GroupePret {

    private String  cod_soc;
    @Id
    private String   cod_grp_pret;
    private String   lib_grp_pret;
    private String  typ_groupe;
    private String  lib_grp_pret_a;

}
