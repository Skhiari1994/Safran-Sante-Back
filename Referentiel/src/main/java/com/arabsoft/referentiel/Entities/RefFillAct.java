package com.arabsoft.referentiel.Entities;

import com.arabsoft.referentiel.Entities.Cle.CleRefFillAct;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@IdClass(CleRefFillAct.class)
public class RefFillAct {
    @Id
    private String cod_fil;
    @Id
    private String   abrv_act;
    private String  val_prix;

}
