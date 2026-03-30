package com.arabsoft.referentiel.Entities;

import com.arabsoft.referentiel.Entities.Cle.BordEnvoiCle;
import com.arabsoft.referentiel.Entities.Cle.CleNatureDonMotif;
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
@IdClass(CleNatureDonMotif.class)
public class NatureDonMotif {
   @Id
   private String nat_don;
   @Id
   private String  cod_affect;

}
