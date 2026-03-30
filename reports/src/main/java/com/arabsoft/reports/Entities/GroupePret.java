package com.arabsoft.reports.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GroupePret {

    private String cod_soc;
    @Id
    private String  cod_grp_pret;
    private String   lib_grp_pret;
    private String  typ_groupe;
    private String lib_grp_pret_a;

}
