package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.AssurActivCle;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "assur_activ")
@IdClass(AssurActivCle.class)
public class AssurActiv {
    @Id
    private String COD_ACTIVITE;
    @Id
    private String COD_ASSUR;
    private Integer AGE_MAX;
    private Integer AGE_ALERT;

}
