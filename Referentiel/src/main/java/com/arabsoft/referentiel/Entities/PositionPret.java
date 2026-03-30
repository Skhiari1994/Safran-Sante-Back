package com.arabsoft.referentiel.Entities;

import com.arabsoft.referentiel.Entities.Cle.ClePositionPret;
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
@IdClass(ClePositionPret.class)
public class PositionPret {

    @Id
    private String  cod_grp_pret;
    @Id
    private String typ_pret;
    @Id
    private String  cod_motif;
    private String  cod_soc;

}
