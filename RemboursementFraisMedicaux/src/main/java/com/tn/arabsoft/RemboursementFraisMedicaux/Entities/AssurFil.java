package com.tn.arabsoft.RemboursementFraisMedicaux.Entities;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.AssurFilCle;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.BordEnvoiCle;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "assur_fil")
@IdClass(AssurFilCle.class)
public class AssurFil {

    @Id
    private String COD_ASSUR;
    @Id
    private String COD_FIL;
    private BigDecimal MNT_ADHER;
    private BigDecimal MNT_ENF;
    private BigDecimal MNT_CONJ;
    private BigDecimal MNT_PERE;
    private BigDecimal MNT_MERE;
    private String PRORAT_PEC;
}
